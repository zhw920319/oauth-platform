package com.taiji.oauthplatform.config.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Author zhw
 * 配置多数据源
 */
@Configuration
public class DataSourcesConfig {

    /***************************************************
     ****             common 库数据源配置              ****
     ***************************************************/
    @Primary
    @Bean(name = "commonProperties")
    @Qualifier("commonProperties")
    @ConfigurationProperties(prefix="datasource.common")
    public DataSourceProperties commonProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "commonDataSource")
    @Qualifier("commonDataSource")
    public DataSource commonDataSource(@Qualifier("commonProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }
}
