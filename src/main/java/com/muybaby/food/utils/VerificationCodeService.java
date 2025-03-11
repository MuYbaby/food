package com.muybaby.food.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {
    
    // 使用ConcurrentHashMap存储邮箱和验证码的映射
    private Map<String, String> codeMap = new ConcurrentHashMap<>();
    // 存储最后一次发送验证码的时间戳
    private Map<String, Long> lastSentTimeMap = new ConcurrentHashMap<>();
    // 使用ScheduledExecutorService实现验证码过期
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    // 验证码有效期（分钟）
    private static final int EXPIRATION_MINUTES = 5;
    // 验证码请求间隔（秒）
    private static final int REQUEST_INTERVAL_SECONDS = 30;
    
    // 检查是否可以发送验证码（是否超过了限制时间）
    public boolean canSendCode(String email) {
        Long lastSentTime = lastSentTimeMap.get(email);
        if (lastSentTime == null) {
            return true;
        }
        
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastSentTime;
        return elapsedTime >= (REQUEST_INTERVAL_SECONDS * 1000);
    }
    
    // 获取剩余等待时间（秒）
    public int getRemainingWaitTime(String email) {
        Long lastSentTime = lastSentTimeMap.get(email);
        if (lastSentTime == null) {
            return 0;
        }
        
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastSentTime;
        int remainingTime = REQUEST_INTERVAL_SECONDS - (int)(elapsedTime / 1000);
        return Math.max(0, remainingTime);
    }
    
    // 保存验证码
    public void saveVerificationCode(String email, String code) {
        codeMap.put(email, code);
        lastSentTimeMap.put(email, System.currentTimeMillis());
        
        // 设置验证码过期时间
        scheduler.schedule(() -> {
            codeMap.remove(email);
        }, EXPIRATION_MINUTES, TimeUnit.MINUTES);
    }
    
    // 验证验证码
    public boolean verifyCode(String email, String code) {
        String savedCode = codeMap.get(email);
        if (savedCode != null && savedCode.equals(code)) {
            // 验证通过后移除验证码
            codeMap.remove(email);
            return true;
        }
        return false;
    }
}