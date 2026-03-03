package com.example.monolithic.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    @Qualifier("tokenRedis")  // 여러 RedisTemplate를 사용할 때의 구분자
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @Qualifier("tokenRedis")  // 여러 RedisTemplate를 사용할 때의 구분자
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory rcf) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // Redis connection factory 주입
        template.setConnectionFactory(rcf);

        // 객체 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }

}
