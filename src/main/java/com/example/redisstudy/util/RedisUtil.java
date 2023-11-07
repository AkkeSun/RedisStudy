package com.example.redisstudy.util;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save (String key, String val) {
        redisTemplate.opsForValue().set(key, val);
    }

    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // keys::* 의 실제 Key 를 추출한다 (50개씩 끊어서 찾는다)
    // redis keys() 함수로 key를 조회하면다른 redis 로직이 블로킹 된다.
    // 이 때 cursor 를 대체하여 사용할 수 있다
    public List<String> scanByPattern(String pattern) {
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions scanOptions = ScanOptions.scanOptions().count(50).match(pattern).build();
        Cursor cursor = redisConnection.scan(scanOptions);

        List<String> redisKeys = new ArrayList<>();
        while (cursor.hasNext()) {
            String key = new String((byte[]) cursor.next());
            redisKeys.add(key);
        }

        return redisKeys;
    }

    public void deleteKeys(List<String> keys) {
        redisTemplate.delete(keys);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

}
