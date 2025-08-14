package com.practice.waitingqueue.common.config;

import com.practice.waitingqueue.domain.entity.WaitingItem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
        var template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(cf);

        var keySer = new StringRedisSerializer();
        var valueSer = new GenericJackson2JsonRedisSerializer();

        template.setKeySerializer(keySer);
        template.setHashKeySerializer(keySer);
        template.setValueSerializer(valueSer);
        template.setHashValueSerializer(valueSer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, WaitingItem> waitingItemRedisTemplate(RedisConnectionFactory cf) {
        var template = new RedisTemplate<String, WaitingItem>();
        template.setConnectionFactory(cf);

        var keySer = new StringRedisSerializer();
        var valueSer = new GenericJackson2JsonRedisSerializer();

        template.setKeySerializer(keySer);
        template.setHashKeySerializer(keySer);
        template.setValueSerializer(valueSer);
        template.setHashValueSerializer(valueSer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory cf) {
        var template = new StringRedisTemplate();
        template.setConnectionFactory(cf);
        template.afterPropertiesSet();
        return template;
    }
}
