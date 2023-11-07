package com.example.redisstudy.service;

import com.example.redisstudy.domain.User;
import com.example.redisstudy.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RedisUtil redisUtil;
    private final CacheChecker cacheChecker;
    private final String keyPrefix = "key::";

    public void save(User user)  {
        String json;
        try {
            ObjectMapper om = new ObjectMapper();
            json = om.writeValueAsString(user);
        } catch (Exception e) {
            json = "";
        }
        redisUtil.save(keyPrefix + user.getId(), json);
    }

    @Cacheable(value="userInfo", key = "#id") // userInfo::id
    public User getUser(int id) {
        cacheChecker.cacheCheck();
        String jsonStr = (String) redisUtil.getData(keyPrefix + id);
        if (ObjectUtils.isEmpty(jsonStr)){
            return null;
        }
        try {
            ObjectMapper om = new ObjectMapper();
            return om.readValue(jsonStr, User.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void delete (int id){
        redisUtil.deleteKey(keyPrefix + id);
    }

}
