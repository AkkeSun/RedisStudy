package com.example.redisstudy;

import com.example.redisstudy.domain.User;
import com.example.redisstudy.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest {

    @Autowired
    UserService userService;

    @AfterEach
    void reset(){
        userService.deleteAll();
    }

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
    }

    @Test
    @DisplayName("redis 데이터가 정상적으로 삭제되는지 테스트")
    void deleteTest() {

        // given
        User user = User.builder()
            .id(99)
            .name("test")
            .build();

        // when
        userService.save(user);
        userService.delete(99);
        User savedUser = userService.getUser(99);

        // then
        Assertions.assertNull(savedUser);
    }

    @Test
    @DisplayName("redis 캐시가 정상적으로 처리되는지 테스트")
    void cacheTest() {

        // given
        User user = User.builder()
            .id(100)
            .name("sun")
            .build();
        userService.save(user);

        // when
        User savedUser = userService.getUser(user.getId());
        User cacheData = userService.getUser(user.getId());

        Assertions.assertEquals(cacheData.getId(), savedUser.getId());
        Assertions.assertEquals(cacheData.getName(), savedUser.getName());
    }


}
