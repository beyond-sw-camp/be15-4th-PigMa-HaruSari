package com.pigma.harusari.user.infrastructure.email.service;

import com.pigma.harusari.user.infrastructure.email.exception.EmailErrorCode;
import com.pigma.harusari.user.infrastructure.email.exception.SignupEmailCodeInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailAuthServiceImpl implements EmailAuthService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender;

    public void sendVerificationCode(String email) {
        // 1. 인증코드 생성
        String code = generateRandomCode();

        // 2. Redis에 이메일-인증코드를 저장
        redisTemplate.opsForValue().set("EMAIL_CODE:" + email, code, Duration.ofMinutes(5));

        // 3. 실제 이메일 발송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[하루살이] 이메일 인증 코드");
        message.setText("인증 코드: " + code + "\n\n5분 내로 입력해주세요.");

        mailSender.send(message);
    }

    public void verifyCode(String email, String code) {
        // redis에 저장된 인증코드 불러오기
        String stored = redisTemplate.opsForValue().get("EMAIL_CODE:" + email);

        // 인증코드 검증
        if (stored == null || !stored.equals(code)) {
            throw new SignupEmailCodeInvalidException(EmailErrorCode.SIGNUP_EMAIL_CODE_INVALID);
        }

        // redis에 저장된 인증코드 삭제 및 검증 완료 여부 저장
        redisTemplate.delete("EMAIL_CODE:" + email);
        redisTemplate.opsForValue().set("EMAIL_VERIFIED:" + email, "true", Duration.ofMinutes(30));
    }

    private String generateRandomCode() {
        int code = new Random().nextInt(900000) + 100000; // 100000 ~ 999999
        return String.valueOf(code);
    }

}
