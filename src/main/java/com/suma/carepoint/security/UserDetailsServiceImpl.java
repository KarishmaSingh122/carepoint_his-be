package com.suma.carepoint.security;

import com.suma.carepoint.entities.auth.User;
import com.suma.carepoint.repositories.auth.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.suma.carepoint.models.constants.ApiConstant.Security.ROLE_;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        Set<SimpleGrantedAuthority> authorities = new LinkedHashSet<>();
        userRepository.findAuthorities(user.getUserId()).stream()
                .flatMap(row -> Stream.concat(
                        Stream.ofNullable(row.getRoleName()).map(this::normalizeRole),
                        Stream.ofNullable(row.getPermissionName())
                ))
                .filter(authority -> !authority.isBlank())
                .distinct()
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

//        if (user.getStaff() == null || user.getStaff().getStaffId() == null) {
//            throw new UsernameNotFoundException("Invalid credentials");
//        }

        return new UserPrincipal(
                user.getUserId(),
                user.getStaff().getStaffId(),
                user.getUsername(),
                user.getPasswordHash(),
                true,
                authorities.stream()
                        .map(a -> (GrantedAuthority) a)
                        .toList()
        );
    }

    private String normalizeRole(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be blank");
        }

        String normalized = roleName.trim().toUpperCase();
        return normalized.startsWith(ROLE_)
                ? normalized
                : ROLE_ + normalized;
    }
}
