package com.suma.carepoint.security;
import com.suma.carepoint.repositories.auth.UserRoleRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.suma.carepoint.entities.auth.User;
import com.suma.carepoint.repositories.auth.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;


@Service
public class CustomUserDetailsService  implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public CustomUserDetailsService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        var authorities = userRoleRepository
                .findByUserUserIdOrderByAssignedAtAsc(user.getUserId())
                .stream()
                .filter(userRole -> userRole.getRole().isActive())
                .map(userRole -> new SimpleGrantedAuthority(
                        "ROLE_" + userRole.getRole().getRoleName().toUpperCase()))
                .collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .disabled(!user.isActive())
                .build();
    }
}
