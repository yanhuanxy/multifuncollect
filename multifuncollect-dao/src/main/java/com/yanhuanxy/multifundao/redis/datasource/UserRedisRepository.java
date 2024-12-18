package com.yanhuanxy.multifundao.redis.datasource;

import com.yanhuanxy.multifundomain.datasource.UserRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRedisRepository extends CrudRepository<UserRedis, Long> {

}
