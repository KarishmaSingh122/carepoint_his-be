
package com.suma.carepoint.security;

import com.suma.carepoint.exceptions.UnauthorizedAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.suma.carepoint.models.constants.ApiConstant.Security.ROLE_;

@Component
public class CurrentUserService {

    public UserPrincipal getActiveContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new UnauthorizedAccessException("No authenticated HIMS user!");
        }

        return principal;
    }

    public Long getCurrentUserId() {
        return getActiveContext().userId();
    }

    public Long getCurrentStaffId() {
        return getActiveContext().staffId();
    }

    public List<String> getCurrentUserRoles() {
        return getActiveContext()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(role -> role.startsWith(ROLE_))
                .map(role -> role.substring(ROLE_.length()))
                .map(role -> role.toUpperCase(Locale.ROOT))
                .toList();
    }

    public boolean hasAnyRole() {
        return !getCurrentUserRoles().isEmpty();
    }

    public boolean hasRole(String role) {
        return getCurrentUserRoles().contains(role.toUpperCase(Locale.ROOT));
    }

    public boolean isSuperAdmin() {
        return hasRole("SUPER_ADMIN");
    }

    public boolean isHospitalAdmin() {
        return hasRole("HOSPITAL_ADMIN");
    }

    public boolean isAdministrator() {
        return isSuperAdmin() || isHospitalAdmin();
    }
}
