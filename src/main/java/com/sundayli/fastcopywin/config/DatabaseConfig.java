package com.sundayli.fastcopywin.config;

import com.sundayli.fastcopywin.model.RecordData;
import java.sql.Statement;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.transaction.TransactionManager;

@Configuration
@ComponentScan(value = "com.sundayli.fastcopywin.repository")
@EnableJdbcRepositories({"com.sundayli.fastcopywin.repository"})
public class DatabaseConfig extends AbstractJdbcConfiguration {

  @Bean
  DataSource dataSource() {
    JdbcDataSource jdbcDataSource = new JdbcDataSource();
    jdbcDataSource.setURL("jdbc:h2:./data/sundayLi;AUTO_SERVER=TRUE");
    jdbcDataSource.setUser("sundayLi");
    jdbcDataSource.setPassword("789456");
    return jdbcDataSource;
  }

  @Bean
  NamedParameterJdbcOperations namedParameterJdbcOperations(DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
  }

  @Bean
  TransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean
  DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
    dataSourceInitializer.setDataSource(dataSource);
    dataSourceInitializer.setEnabled(true);
    dataSourceInitializer.setDatabasePopulator(connection -> {
      Statement statement = connection.createStatement();
      statement.execute(RecordData.getCreateTableSql());
    });
    return dataSourceInitializer;
  }

}
