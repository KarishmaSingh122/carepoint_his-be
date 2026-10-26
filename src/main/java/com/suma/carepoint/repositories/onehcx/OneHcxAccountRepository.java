package com.suma.carepoint.repositories.onehcx;

import com.suma.carepoint.entities.onehcx.OneHcxAccount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OneHcxAccountRepository
        extends JpaRepository<OneHcxAccount, Long> {

    Optional<OneHcxAccount> findByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM OneHcxAccount a WHERE a.staff.staffId = :staffId")
    Optional<OneHcxAccount> findByStaffIdForUpdate(@Param("staffId") Long staffId);

    @Query("""
        SELECT a
        FROM OneHcxAccount a
        WHERE a.staff.staffId = :staffId
        """)
    Optional<OneHcxAccount> findByStaffId(@Param("staffId") Long staffId);

}
