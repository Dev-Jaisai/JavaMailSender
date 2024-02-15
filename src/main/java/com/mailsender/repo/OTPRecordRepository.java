package com.mailsender.repo;

import com.mailsender.entity.OTPRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OTPRecordRepository extends JpaRepository<OTPRecordEntity, Long> {
    OTPRecordEntity findByUserId(String userId);

    @Query("SELECT o FROM OTPRecordEntity o WHERE o.expiry < ?1")
    List<OTPRecordEntity> findExpiredRecords(Instant currentInstant);
}
