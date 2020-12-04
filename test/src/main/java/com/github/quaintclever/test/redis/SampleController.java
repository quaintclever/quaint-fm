package com.github.quaintclever.test.redis;

import com.github.quaintclever.cache.component.RedisService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * desc:
 * </p>
 *
 * @author quaint
 * @since 04 December 2020
 */
@RestController
public class SampleController {

    @Data
    private static class CacheDto {
        private String keyName;
        private String value;
    }

    @Autowired
    RedisService redisService;

    @PostMapping("setKey")
    public String setRedisKey(@RequestBody CacheDto cacheDto) {
        redisService.set(cacheDto.getKeyName(), cacheDto.getValue());
        return "ok";
    }

    @PostMapping("getKey")
    public String getRedisKey(@RequestBody String key) {
        Object value = redisService.get(key);
        if (value != null) {
            return value.toString();
        } else {
            return "fail null";
        }
    }
}
