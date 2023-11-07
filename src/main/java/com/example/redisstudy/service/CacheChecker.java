package com.example.redisstudy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheChecker {

    public void cacheCheck() {
        log.info("캐시를 거치지 않은 데이터 입니다");
    }
}
