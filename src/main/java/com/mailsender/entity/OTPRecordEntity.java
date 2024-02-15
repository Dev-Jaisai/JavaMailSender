package com.mailsender.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "otp_records")
public class OTPRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "hashed_otp", nullable = false)
    private String hashedOTP;

    @Column(name = "expiry", nullable = false)
    private Instant expiry;

    public OTPRecordEntity(String userId, String hashedOTP, Instant expiry) {
        this.userId = userId;
        this.hashedOTP = hashedOTP;
        this.expiry = expiry;
    }
}
