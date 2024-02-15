package com.mailsender.service;

import com.mailsender.entity.OTPRecordEntity;
import com.mailsender.repo.OTPRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OTPService {
    @Autowired
    private OTPRecordRepository otpRecordRepository; // Assuming you have an OTPRecordRepository interface

    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALIDITY_MINUTES = 1;
    private static final SecureRandom random = new SecureRandom();

    // Replace with your database/storage mechanism
    private Map<String, OTPRecordEntity> otpStorage = new HashMap<>();

    public String generateOTP(String userId) {
        String otp = generateRandomOTP();

        // Replace with your preferred hashing mechanism
        String hashedOTP = hashOTP(otp);
        Instant expiry = generateExpiry();
        OTPRecordEntity otpRecordEntity = new OTPRecordEntity(userId, hashedOTP, expiry);
        otpRecordRepository.save(otpRecordEntity);

        return otp; // Send this OTP to the user (SMS, email, etc.)
    }
    public boolean validateOTP(String userId, String providedOTP) {
        OTPRecordEntity otpRecordEntity = otpRecordRepository.findByUserId(userId);

        if (otpRecordEntity == null || otpRecordEntity.isExpired()) {
            return false;
        }

        boolean isValid = compareHashedOTP(providedOTP, otpRecordEntity.getHashedOTP());

        if (isValid) {
            otpRecordRepository.delete(otpRecordEntity); // Cleanup after validation
        }

        return isValid;
    }

    private String generateRandomOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10)); // Generate random digit (0-9)
        }
        return otp.toString();
    }

    private Instant generateExpiry() {
        return Instant.now().plus(OTP_VALIDITY_MINUTES, ChronoUnit.MINUTES);
    }

    private String hashOTP(String otp) {
        return BCrypt.hashpw(otp, BCrypt.gensalt());
    }

    private boolean compareHashedOTP(String providedOTP, String hashedOTP) {
        return BCrypt.checkpw(providedOTP, hashedOTP);
    }

    @Scheduled(cron = "0 */1 * * * *") // Runs every 1 minutes
    public void deleteExpiredOTPRecordsScheduled() {
        log.info("Into schedular");
        deleteExpiredOTPRecords();
    }
    public void deleteExpiredOTPRecords() {
        List<OTPRecordEntity> expiredRecords = otpRecordRepository.findExpiredRecords(Instant.now());
        log.info("deletd after expiration of otps");

        otpRecordRepository.deleteAll(expiredRecords);
    }
}