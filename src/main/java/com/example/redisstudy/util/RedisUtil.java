package com.example.redisstudy.util;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public void save (String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public Map<Object, Object> getData(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void deleteKeys(List<String> keys) {
        redisTemplate.delete(keys);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

}
