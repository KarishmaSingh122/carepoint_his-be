package com.suma.carepoint.repositories.auth;

import com.suma.carepoint.entities.auth.RolePermission;
import com.suma.carepoint.entities.auth.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {

    List<RolePermission> findByRoleRoleIdOrderByPermissionPermissionNameAsc(Long roleId);

    List<RolePermission> findByPermissionPermissionIdOrderByRoleRoleNameAsc(Long permissionId);

    Optional<RolePermission> findByRoleRoleIdAndPermissionPermissionId(Long roleId, Long permissionId);

    boolean existsByRoleRoleIdAndPermissionPermissionId(Long roleId, Long permissionId);

    long countByRoleRoleId(Long roleId);

    long countByPermissionPermissionId(Long permissionId);
}
