package com.chatapp.chatapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RedisConnectionTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void redisConnectionWorks() {
        redisTemplate.opsForValue().set("testKey", "hello");
        String value = redisTemplate.opsForValue().get("testKey");

        assertEquals("hello", value);
    }
}
