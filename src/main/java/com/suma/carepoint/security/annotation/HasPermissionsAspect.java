package com.suma.carepoint.security.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class HasPermissionsAspect {

    @Around("@annotation(hasPermissions)")
    public Object checkPermissions(ProceedingJoinPoint joinPoint,
                                   HasPermissions hasPermissions) throws Throwable {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentication required");
        }

        Set<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        for (String permission : hasPermissions.value()) {
            if (!authorities.contains(permission)) {
                throw new AccessDeniedException("Missing permission: " + permission);
            }
        }

        return joinPoint.proceed();
    }
}
