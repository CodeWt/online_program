package com.example.online_program.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wtt
 * @Date: 20-5-9
 * @Description:
 */
@Configuration
public class LogConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogConfig.class);
    @Test
    @Bean
    public void disLog(){
        LOGGER.info("===============");
    }
}
