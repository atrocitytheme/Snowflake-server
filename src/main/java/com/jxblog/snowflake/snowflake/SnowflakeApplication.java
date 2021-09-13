package com.jxblog.snowflake.snowflake;

import com.jxblog.snowflake.snowflake.context.IdempotentChecker;
import com.jxblog.snowflake.snowflake.context.ProcessContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SnowflakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnowflakeApplication.class, args);
    }

    @Bean
    public IdempotentChecker<String, String> processContext() {
        return new ProcessContext<>();
    }
}
