package com.suma.carepoint.security;

import com.suma.carepoint.exceptions.UnauthorizedAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserService {

    public UserPrincipal getActiveContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new UnauthorizedAccessException("No authenticated HIMS user!");
        }
        return (UserPrincipal) authentication.getPrincipal();
    }

    public Long getCurrentUserId() {
        return getActiveContext().userId();
    }
}
