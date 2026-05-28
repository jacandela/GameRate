package com.gamerate.gamerate.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.pattern.ValidatePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class FlywayConfig
{
    @Autowired
    private Environment environment;

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource)
    {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource) // Configurado en properties
                .locations("classpath:db/migration") // Configurado en properties
                .baselineOnMigrate(true) // Configurado en properties
                .ignoreMigrationPatterns(
                        // Cuando un script ya no existe en el código fuente
                        ValidatePattern.fromPattern("*:missing"),
                        ValidatePattern.fromPattern("*:future"))
                .load();

        if (isDevProfile()) {
            flyway.repair();
        }

        flyway.migrate();

        return flyway;
    }

    private boolean isDevProfile() {
        String[] profiles = environment.getActiveProfiles();

        // Si no hay profiles activos, usa los por defecto
        if (profiles.length == 0) {
            profiles = environment.getDefaultProfiles();
        }

        return Arrays.asList(profiles).contains("dev");
    }

    /*@Bean
    public ApplicationRunner repairFlywayOnStartup() {
        return args -> {
            // ⚠️ Solo si realmente necesitas reparar el esquema
            flyway.repair();
        };
    }*/
}
