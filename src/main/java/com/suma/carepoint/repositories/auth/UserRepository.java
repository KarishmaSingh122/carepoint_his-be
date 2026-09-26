package com.suma.carepoint.repositories.auth;

import com.suma.carepoint.entities.auth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
