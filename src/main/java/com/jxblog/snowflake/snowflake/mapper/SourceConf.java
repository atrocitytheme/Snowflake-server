package com.jxblog.snowflake.snowflake.mapper;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SourceConf {
    private static Logger logger = LogManager.getLogger(SourceConf.class);
    @Value("${mysql.driver}")
    private String driverName;
    @Value("${mysql.user}")
    private String user;
    @Value("${mysql.password}")
    private String password;
    @Value("${mysql.url}")
    private String url;

    @Bean
    public DataSource singleConnDataSource() {
        DataSourceBuilder builder= DataSourceBuilder.create();
        logger.info("Driver name: " + driverName);
        builder.driverClassName(driverName);
        builder.url(url);
        builder.username(user);
        builder.password(password);
        return builder.build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("singleConnDataSource") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        return sessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate connectionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory fac) {
        return new SqlSessionTemplate(fac);
    }
}
