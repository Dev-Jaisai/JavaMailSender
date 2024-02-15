package com.mailsender.repo;

import com.mailsender.entity.OTPRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPRecordRepository extends JpaRepository<OTPRecordEntity,Long> {
}
