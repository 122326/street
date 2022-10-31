package com.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author kai
 * @create 2022-10-6 8:53
 */
@Configuration
public class RedisConfig {

    /**
     * 配置redis的模板类
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //初始化redis模板
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //建立redis连接
        template.setConnectionFactory(redisConnectionFactory);
        // json序列号
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        serializer.setObjectMapper(mapper);
        //string序列号
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用string的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // Hash的key 采用string的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value采用Jackson2JsonRedisSerializer的序列化方式
        template.setValueSerializer(serializer);
        // Hash的value 采用Jackson2JsonRedisSerializer的序列化方式
        template.setHashValueSerializer(serializer);
        // 配置具体的序列化方式
        template.afterPropertiesSet();
        return template;
    }
}
