package com.bjtu.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    @Bean("Counter")
    public Counter configCounter(){
        return new Counter();
    }

    @Bean("JSONConfig")
    public JSONConfig JSONConfig(){
        return new JSONConfig();
    }
}