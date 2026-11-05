package com.suma.carepoint.repositories.auth;

import com.suma.carepoint.entities.auth.UserRole;
import com.suma.carepoint.entities.auth.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    List<UserRole> findByUserUserIdOrderByAssignedAtAsc(Long userId);

    List<UserRole> findByRoleRoleIdOrderByAssignedAtAsc(Long roleId);

    Optional<UserRole> findByUserUserIdAndRoleRoleId(Long userId, Long roleId);

    boolean existsByUserUserIdAndRoleRoleId(Long userId, Long roleId);

    long countByRoleRoleId(Long roleId);

    long countByUserUserId(Long userId);

    @Query("""
            SELECT s.staffId AS staffId,
                   r.roleName AS roleName
            FROM Staff s
            JOIN User u ON u.staff.staffId = s.staffId
            JOIN UserRole ur ON ur.user.userId = u.userId
            JOIN Role r ON r.roleId = ur.role.roleId
            WHERE s.staffId IN :staffIds
            """)
    List<StaffRoleProjection> findStaffRoles(@Param("staffIds") List<Long> staffIds);
}
