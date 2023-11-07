package com.example.redisstudy;

import static org.mockito.Mockito.verify;

import com.example.redisstudy.domain.User;
import com.example.redisstudy.service.CacheChecker;
import com.example.redisstudy.service.UserService;
import com.example.redisstudy.util.RedisUtil;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class RedisTest {

    @Autowired
    UserService userService;

    @MockBean
    CacheChecker cacheChecker;

    @Autowired
    RedisUtil redisUtil;

    @Test
    @DisplayName("redis 데이터가 정상적으로 저장되고 조회되는지 테스트")
    void saveTest() {
        // given
        User user = User.builder()
            .id(10)
            .name("test")
            .build();

        // when
        userService.save(user);
        User savedUser = userService.getUser(user.getId());

        // then
        Assertions.assertEquals(user.getId(), savedUser.getId());
        Assertions.assertEquals(user.getName(), savedUser.getName());
        userService.delete(user.getId());
    }

    @Test
    @DisplayName("redis 캐시가 정상적으로 처리되는지 테스트")
    void cacheTest() {

        User user = User.builder()
            .id(100)
            .name("sun")
            .build();
        userService.save(user);

        // when
        User savedUser = userService.getUser(user.getId());
        User cacheData = userService.getUser(user.getId());

        verify(cacheChecker, Mockito.times(1)).cacheCheck();
        Assertions.assertEquals(cacheData.getId(), savedUser.getId());
        Assertions.assertEquals(cacheData.getName(), savedUser.getName());
        userService.delete(user.getId());
    }

    @Test
    @DisplayName("redis 데이터가 정상적으로 삭제되는지 테스트")
    void deleteTest() {

        // given
        User user = User.builder()
            .id(10)
            .name("test")
            .build();
        User user2 = User.builder()
            .id(11)
            .name("test")
            .build();
        User user3 = User.builder()
            .id(12)
            .name("test")
            .build();
        userService.save(user);
        userService.save(user2);
        userService.save(user3);

        // when
        List<String> keys = redisUtil.scanByPattern("key::*");
        redisUtil.deleteKeys(keys);

        // then
        User savedUser = userService.getUser(user.getId());
        User savedUser2 = userService.getUser(user2.getId());
        User savedUser3 = userService.getUser(user3.getId());
        Assertions.assertEquals(keys.size(), 3);
        Assertions.assertNull(savedUser);
        Assertions.assertNull(savedUser2);
        Assertions.assertNull(savedUser3);
    }
}
