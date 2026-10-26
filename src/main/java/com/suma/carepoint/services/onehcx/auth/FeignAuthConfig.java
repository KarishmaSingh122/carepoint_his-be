
package com.suma.carepoint.services.onehcx.auth;

import com.suma.carepoint.entities.onehcx.OneHcxAccount;
import com.suma.carepoint.repositories.onehcx.OneHcxAccountRepository;

import com.suma.carepoint.security.UserPrincipal;
import feign.RequestInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FeignAuthConfig {

    private final OneHcxAccountRepository repository;


    @Bean
    public RequestInterceptor authInterceptor() {
        return template -> {
            String url = template.url();
            log.debug("Feign request: method={}, url={}", template.method(), url);
            if (url.contains("/login")) {
                return;
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null
                    || !authentication.isAuthenticated()
                    || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
                log.debug("No authenticated user; skipping authentication");
                return;
            }
            long accountId = principal.userId();
            OneHcxAccount account = repository.findByStaffId(accountId).orElse(null);

            if (account == null || account.getTokenValue() == null || account.getTokenValue().isBlank()) {
                log.debug("No stored token; skipping authentication");
                return;
            }

            template.removeHeader("Authorization");
//            template.removeHeader("X-User-ID");
            template.header("Authorization", account.getTokenType().trim() + " " + account.getTokenValue());
//            template.header("X-User-ID", String.valueOf(4));
        };
    }

}
