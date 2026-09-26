package com.suma.carepoint.repositories.auth;

import com.suma.carepoint.entities.auth.UserRole;
import com.suma.carepoint.entities.auth.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
