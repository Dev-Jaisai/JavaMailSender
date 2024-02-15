package com.mailsender.service;

import com.mailsender.entity.OTPRecordEntity;
import com.mailsender.repo.OTPRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
public class OTPService {
    @Autowired
    private OTPRecordRepository otpRecordRepository; // Assuming you have an OTPRecordRepository interface

    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALIDITY_MINUTES = 5;
    private static final SecureRandom random = new SecureRandom();

    // Replace with your database/storage mechanism
    private Map<String, OTPRecord> otpStorage = new HashMap<>();

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
        OTPRecord otpRecord = otpStorage.get(userId);

        if (otpRecord == null || otpRecord.isExpired()) {
            return false;
        }

        boolean isValid = compareHashedOTP(providedOTP, otpRecord.getHashedOTP());

        if (isValid) {
            otpStorage.remove(userId); // Cleanup after validation
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

    // Placeholder - Implement strong hashing
    private String hashOTP(String otp) {
        // TODO: Use bcrypt, Argon2, etc.
        return BCrypt.hashpw(otp, BCrypt.gensalt());
    }

    // Placeholder - Implement secure comparison
    private boolean compareHashedOTP(String providedOTP, String hashedOTP) {
        // TODO: Constant-time comparison
        return hashedOTP.equals(providedOTP); // Remove in production
    }

    private static class OTPRecord {
        private final String userId;
        private final String hashedOTP;
        private final Instant expiry;

        public OTPRecord(String userId, String hashedOTP, Instant expiry) {
            this.userId = userId;
            this.hashedOTP = hashedOTP;
            this.expiry = expiry;
        }

        public boolean isExpired() {
            return Instant.now().isAfter(expiry);
        }

        public String getUserId() {
            return userId;
        }

        public String getHashedOTP() {
            return hashedOTP;
        }
    }
}
