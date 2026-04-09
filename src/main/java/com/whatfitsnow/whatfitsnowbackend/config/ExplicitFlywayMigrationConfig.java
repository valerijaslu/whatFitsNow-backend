package com.whatfitsnow.whatfitsnowbackend.config;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Explicit Flyway trigger to ensure migrations happen before Hibernate validation.
 * This avoids relying on auto-configuration ordering.
 */
@Configuration
@Profile("local")
public class ExplicitFlywayMigrationConfig {

  @Bean(name = "explicitFlywayMigrator")
  InitializingBean explicitFlywayMigrator(DataSource dataSource) {
    return () -> {
      Flyway flyway = Flyway.configure()
          .dataSource(dataSource)
          .locations("classpath:db/migration")
          .baselineOnMigrate(true)
          .baselineVersion("0")
          .load();
      flyway.migrate();
    };
  }
}

