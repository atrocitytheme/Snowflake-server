package com.jxblog.snowflake.snowflake;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.embedded.jetty.ConfigurableJettyWebServerFactory;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
@MapperScan("com.jxblog.snowflake.snowflake")
public class ApiContainer {

    @Bean
    public ConfigurableJettyWebServerFactory webServerFactory() {
        JettyServletWebServerFactory fac = new JettyServletWebServerFactory();
        fac.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/errors.html"));
        return fac;
    }
}
