package com.suma.carepoint.repositories.auth;

import com.suma.carepoint.entities.auth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByStaffStaffId(Long staffId);

    Optional<User> findByStaffStaffId(Long staffId);

    Page<User> findByActive(boolean active, Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    Page<User> findByActiveAndUsernameContainingIgnoreCase(
            boolean active, String username, Pageable pageable);

    Optional<User> findByUserId(Long uploadedBy);

    Optional<User> findByUsernameAndActiveTrue(String username);

    @Query("""
                SELECT DISTINCT
                    r.roleName AS roleName,
                    p.permissionName AS permissionName
                FROM UserRole ur
                JOIN ur.role r
                JOIN RolePermission rp ON rp.role = r
                JOIN rp.permission p
                WHERE ur.id.userId = :userId
                  AND ur.user.active = true
                  AND r.active = true
            """)
    List<AuthorityProjection> findAuthorities(@Param("userId") Long userId);

    @Query("""
                 SELECT DISTINCT r.roleName
                    FROM UserRole ur
                    JOIN ur.role r
                    JOIN ur.user u
                    WHERE u.userId = :userId
                      AND u.active = true
                      AND r.active = true
            """)
    List<String> findActiveRoleNames(@Param("userId") Long userId);

    @Query("""
                SELECT DISTINCT p.permissionName
                FROM UserRole ur
                JOIN ur.role r
                JOIN RolePermission rp on rp.role = r
                JOIN rp.permission p
                WHERE ur.user.userId = :userId
                  AND ur.user.active = true
                  AND r.active = true
            """)
    List<String> findActivePermissionNames(@Param("userId") Long userId);
}
