package com.example.demo.service.redis;

import com.example.demo.exeption.jwt.WrongRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void setValues(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setValues(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public String getValues(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    public void validateRefreshToken(String username, String refreshToken) {
        String tokenValue = this.getValues(username);

        if(!tokenValue.equals(refreshToken)) {
            throw new WrongRefreshTokenException();
        }
    }
}
