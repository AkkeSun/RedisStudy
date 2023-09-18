package com.example.redisstudy.service;

import com.example.redisstudy.domain.User;
import com.example.redisstudy.util.RedisUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RedisUtil redisUtil;
    private final String keyPrefix = "key";

    public void save(User user){
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("name", user.getName());
        String key = keyPrefix + ":"+ user.getId();
        redisUtil.save(key, map);
    }

    @Cacheable(value="userInfo", key = "#id") // userInfo:id
    public User getUser(int id) {
        System.out.println("캐시를 거치지 않은 데이터 입니다");

        String key = keyPrefix + ":"+ id;
        Map<Object, Object> data = redisUtil.getData(key);
        if (ObjectUtils.isEmpty(data)){
            return null;
        }
        return User.builder()
            .id((Integer)data.get("id"))
            .name(data.get("name").toString())
            .build();
    }

    public void delete (int id){
        redisUtil.deleteKey(keyPrefix + ":" +id);
    }

    public void deleteAll (){
        redisUtil.deleteKey(keyPrefix);
    }
}
