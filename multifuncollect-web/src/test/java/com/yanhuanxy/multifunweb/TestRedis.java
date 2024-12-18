package com.yanhuanxy.multifunweb;

import com.yanhuanxy.multifundao.redis.datasource.UserRedisRepository;
import com.yanhuanxy.multifundomain.datasource.UserRedis;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRedis {

    @Resource
    private UserRedisRepository userRedisRepository;

    @Test
    public void test() {
        UserRedis userRedis = new UserRedis();
        userRedis.setId(2L);
        userRedis.setName("test2");
        userRedis.setEmail("182@qq.com");
        userRedis.setPassword("9899999");
        userRedisRepository.save(userRedis);
    }

}
