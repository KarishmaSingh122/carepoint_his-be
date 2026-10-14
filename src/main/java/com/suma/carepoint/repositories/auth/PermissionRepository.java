package com.suma.carepoint.repositories.auth;

import com.suma.carepoint.entities.auth.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByPermissionNameIgnoreCase(String permissionName);

    boolean existsByPermissionNameIgnoreCase(String permissionName);

    Page<Permission> findByPermissionNameContainingIgnoreCase(String permissionName, Pageable pageable);

    Page<Permission> findByModuleNameContainingIgnoreCase(String moduleName, Pageable pageable);

    Page<Permission> findByActionIgnoreCase(String action, Pageable pageable);

    Page<Permission> findByModuleNameContainingIgnoreCaseAndActionIgnoreCase(
            String moduleName, String action, Pageable pageable);
}
