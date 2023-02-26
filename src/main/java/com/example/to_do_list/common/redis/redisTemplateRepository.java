package com.example.to_do_list.common.redis;

import com.example.to_do_list.common.exception.BusinessLogicException;
import com.example.to_do_list.common.exception.ExceptionCode;
import com.example.to_do_list.domain.RefreshToken;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class redisTemplateRepository {
    private final RedisTemplate redisTemplate;

    public redisTemplateRepository(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(final RefreshToken refreshToken) {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.getRefreshToken(), refreshToken.getUsersId());
        redisTemplate.expire(refreshToken.getRefreshToken(), 60L * 60  * 24, TimeUnit.SECONDS);
    }
    public Optional<RefreshToken> findById(final String refreshToken) {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        Long usersId = valueOperations.get(refreshToken);

        if(Objects.isNull(usersId)) {
            return Optional.empty();
        }
        return Optional.of(new RefreshToken(refreshToken, usersId));
    }

    public void saveAuthCode(String email,String authCode) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(email, authCode);
        redisTemplate.expire(email, 60L * 10, TimeUnit.SECONDS);
    }

    public String findByEmail(String email) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String authCode = valueOperations.get(email);

        if(Objects.isNull(authCode)) {
            throw new BusinessLogicException(ExceptionCode.AUTHCODE_NOT_FOUND);
        }
        return authCode;
    }
}
