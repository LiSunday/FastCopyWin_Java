package com.example.fastcopywin.config;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories({"com.example.fastcopywin.repository"})
public class DatabaseConfig extends AbstractJdbcConfiguration {

  @Bean
  DataSource dataSource() {
    JdbcDataSource jdbcDataSource = new JdbcDataSource();
    jdbcDataSource.setURL("jdbc:h2:./data/test;AUTO_SERVER=TRUE");
    jdbcDataSource.setUser("sundayLi");
    jdbcDataSource.setPassword("789456");
    return jdbcDataSource;
  }

  @Bean
  NamedParameterJdbcOperations namedParameterJdbcOperations(DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
  }

}
