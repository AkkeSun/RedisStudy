package com.example.redisstudy.service;

import com.example.redisstudy.domain.Order;
import com.example.redisstudy.util.RedisUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final RedisUtil redisUtil;
    private final String keyPrefix = "orderInfo";

    public void save(Order order){
        Map<String, Object> map = new HashMap<>();
        map.put("id", order.getId());
        map.put("name", order.getName());
        String key = keyPrefix + ":" +order.getId();
        redisUtil.save(key, map);
    }

    @Cacheable(value="orderInfo", key = "#id") // orderInfo::id
    public Order getOrder(int id) {
        System.out.println("캐시를 거치지 않은 데이터 입니다");

        String key = keyPrefix + ":"+ id;
        Map<Object, Object> data = redisUtil.getData(key);
        if (ObjectUtils.isEmpty(data)){
            return null;
        }
        return Order.builder()
            .id((Integer)data.get("id"))
            .name(data.get("name").toString())
            .build();
    }

    public void delete (){
        redisUtil.deleteKey(keyPrefix);
    }

}
