package com.suma.carepoint.repositories.auth;

import com.suma.carepoint.entities.auth.RefreshToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT t FROM RefreshToken t
                WHERE t.tokenHash = :tokenHash
            """)
    Optional<RefreshToken> findByTokenHashForUpdate(@Param("tokenHash") String tokenHash);

    List<RefreshToken> findAllByFamilyId(UUID familyId);

    @Query("""
                SELECT t FROM RefreshToken t
                WHERE t.familyId = :familyId
                  AND t.revokedAt IS NULL
            """)
    List<RefreshToken> findActiveByFamilyId(@Param("familyId") UUID familyId);
}
