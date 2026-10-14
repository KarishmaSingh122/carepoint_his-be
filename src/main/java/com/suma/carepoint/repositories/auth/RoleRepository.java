package com.suma.carepoint.repositories.auth;

import com.suma.carepoint.entities.auth.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleNameIgnoreCase(String roleName);

    boolean existsByRoleNameIgnoreCase(String roleName);

    Page<Role> findByActive(boolean active, Pageable pageable);

    Page<Role> findByRoleNameContainingIgnoreCase(String roleName, Pageable pageable);

    Page<Role> findByActiveAndRoleNameContainingIgnoreCase(
            boolean active, String roleName, Pageable pageable);
}
