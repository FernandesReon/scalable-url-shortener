package com.reon.userservice.service.impl;

import com.reon.userservice.service.OtpCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class OtpCacheImpl implements OtpCache {
    private final Logger log = LoggerFactory.getLogger(OtpCacheImpl.class);

    private static final String OTP_PREFIX = "otp_";
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder encoder;

    public OtpCacheImpl(RedisTemplate<String, String> redisTemplate, PasswordEncoder encoder) {
        this.redisTemplate = redisTemplate;
        this.encoder = encoder;
    }

    @Override
    public void storeOtp(String otp, String email, Long duration) {
        log.info("OtpCache Service :: Storing otp for user: {}", email);
        redisTemplate.opsForValue()
                .set(buildOtpKey(email), Objects.requireNonNull(encoder.encode(otp)), duration, TimeUnit.MINUTES);
        log.info("OtpCache Service :: Otp stored in cache successfully");
    }

    @Override
    public String getOtp(String email) {
        log.info("OtpCache Service :: Fetching otp for user: {}", email);
        String otp = redisTemplate.opsForValue().get(buildOtpKey(email));
        log.info("OtpCache Service :: Otp fetched successfully");
        return otp;
    }

    @Override
    public void deleteOtp(String email) {
        log.info("OtpCache Service :: Deleting otp for user: {}", email);
        redisTemplate.delete(buildOtpKey(email));
        log.info("OtpCache Service :: Otp deleted from cache successfully");
    }

    private String buildOtpKey(String email) {
        return OTP_PREFIX + email;
    }
}
