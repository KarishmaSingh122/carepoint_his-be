package com.suma.carepoint.services.onehcx.auth;


import com.suma.carepoint.entities.onehcx.OneHcxAccount;
import com.suma.carepoint.entities.onehcx.OneHcxAuthStatus;
import com.suma.carepoint.entities.organization.Staff;
import com.suma.carepoint.exceptions.OneHcxAuthenticationException;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.exceptions.UnauthorizedAccessException;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.auth.LoginRequest;
import com.suma.carepoint.models.onehcx.OneHcxLoginResponse;
import com.suma.carepoint.repositories.onehcx.OneHcxAccountRepository;
import com.suma.carepoint.repositories.organization.StaffRepository;
import com.suma.carepoint.security.CurrentUserService;
import com.suma.carepoint.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class OneHcxAuthService {

    private final OneHcxAccountRepository repository;
    private final CredentialEncryptionService encryptionService;
    private final OneHcxAuthClient hcxClient;
    private final StaffRepository staffRepository;
    private final CurrentUserService currentUserService;


    @Transactional
    public Long configureAccount(String username, String password,Long staffId) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Username and password are required");
        }

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        OneHcxAccount account = repository.findByUsername(username)
                .orElseGet(() -> OneHcxAccount.builder().username(username).authStatus(OneHcxAuthStatus.LOGGED_OUT).build());
        boolean passwordChanged = account.getPassword() == null || !password.equals(encryptionService.decrypt(account.getPassword()));

        if (passwordChanged) {
            account.setPassword(encryptionService.encrypt(password));
            account.setLastPasswordChange(LocalDateTime.now());
        }

        account.setTokenValue(null);
        account.setTokenType(null);
        account.setTokenExpiry(null);
        account.setAuthStatus(OneHcxAuthStatus.LOGGED_OUT);
        account.setLastError(null);
        account.setStaff(staff);

        return repository.save(account).getId();
    }


    @Transactional
    public void login() {
        long accountId = currentUserService.getCurrentUserId();
        OneHcxAccount account = repository.findByStaffId(accountId)
                .orElseThrow(() -> new OneHcxAuthenticationException("OneHcx account not found"));
        String password = decryptPassword(account);
        OneHcxLoginResponse response = requestLogin(account.getUsername(), password);
        validateLoginResponse(response);
        replaceToken(accountId, response.data().token(), response.data().type(), null);
    }

    @Transactional
    public String getValidToken() {
        long accountId = currentUserService.getCurrentUserId();
        OneHcxAccount account = repository.findByStaffId(accountId)
                .orElseThrow(() -> new OneHcxAuthenticationException("OneHcx account not found"));

        if (account.getAuthStatus() == OneHcxAuthStatus.DISABLED) {
            throw new OneHcxAuthenticationException("OneHcx account is disabled");
        }

        if (account.getAuthStatus() == OneHcxAuthStatus.ERROR) {
            throw new OneHcxAuthenticationException("OneHcx account is in an error state");
        }

        boolean missingToken = account.getTokenValue() == null || account.getTokenValue().isBlank();

        boolean expired = account.getTokenExpiry() != null
                && !account.getTokenExpiry()
                .isAfter(LocalDateTime.now().plusSeconds(30));

        boolean loggedOut = account.getAuthStatus() == OneHcxAuthStatus.LOGGED_OUT;

        if (missingToken || expired || loggedOut) {
            login();

            return repository.findByStaffId(accountId)
                    .map(OneHcxAccount::getTokenValue)
                    .filter(token -> !token.isBlank())
                    .orElseThrow(() -> new OneHcxAuthenticationException("Token was not persisted after login"));
        }

        return account.getTokenValue();
    }


    @Transactional
    public void replaceToken(Long accountId, String token, String tokenType, LocalDateTime expiry) {
        if (token == null || token.isBlank()) {
            throw new OneHcxAuthenticationException("Provider returned an empty token");
        }

        OneHcxAccount account = repository.findByStaffIdForUpdate(accountId)
                .orElseThrow(() -> new OneHcxAuthenticationException("OneHcx account not found"));

        account.setTokenValue(token);
        account.setTokenType(tokenType == null || tokenType.isBlank() ? "Bearer" : tokenType);
        account.setTokenExpiry(expiry);
        account.setLastTokenRefresh(LocalDateTime.now());
        account.setLastError(null);
        account.setAuthStatus(OneHcxAuthStatus.ACTIVE);

        if (account.getLastLogin() == null) {
            account.setLastLogin(LocalDateTime.now());
        }

        repository.save(account);
    }

    @Transactional
    public void logout() {
        long accountId = currentUserService.getCurrentUserId();
        OneHcxAccount account = repository.findByStaffId(accountId)
                .orElseThrow(() -> new OneHcxAuthenticationException("OneHcx account not found"));

        if (account.getTokenValue() != null && !account.getTokenValue().isBlank()) {
            try {
                ApiResponse oneHcxLoginResponse = requestLogout();
            } catch (Exception e) {
                throw new OneHcxAuthenticationException("Remote logout failed; local token was retained", e);
            }
        }
        clearLocalToken(accountId);
    }

    private OneHcxLoginResponse requestLogin(String username, String password) {
        try {
            return hcxClient.login(new LoginRequest(username, password));
        } catch (Exception e) {
            throw new OneHcxAuthenticationException("OneHcx login request failed", e);
        }
    }

    @Transactional
    private ApiResponse requestLogout() {
        return hcxClient.logout();
    }

    private void validateLoginResponse(OneHcxLoginResponse response) {
        if (response == null
                || response.data() == null
                || response.data().token() == null
                || response.data().token().isBlank()) {
            throw new OneHcxAuthenticationException("OneHcx login returned no token");
        }

        if (response.status() < 200 || response.status() >= 300) {
            throw new OneHcxAuthenticationException("OneHcx login failed: " + response.message());
        }
    }

    private String decryptPassword(OneHcxAccount account) {
        if (account.getPassword() == null) {
            throw new OneHcxAuthenticationException("No saved password for this OneHcx account");
        }
        return encryptionService.decrypt(account.getPassword());
    }

    @Transactional
    protected void clearLocalToken(Long accountId) {
        OneHcxAccount account = repository.findByStaffIdForUpdate(accountId)
                .orElseThrow(() -> new OneHcxAuthenticationException("OneHcx account not found"));

        account.setTokenValue(null);
        account.setTokenType(null);
        account.setTokenExpiry(null);
        account.setAuthStatus(OneHcxAuthStatus.LOGGED_OUT);
        account.setLastError(null);

        repository.save(account);
    }

    @Transactional
    public ApiResponse getOnehcxUser(Long userId) {
        getValidToken();
        ApiResponse userDetailsByToken = hcxClient.getUserDetailsByToken(userId);
        return userDetailsByToken;
    }

}
