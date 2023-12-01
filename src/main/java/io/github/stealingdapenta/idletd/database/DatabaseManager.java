package io.github.stealingdapenta.idletd.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Objects;


public class DatabaseManager {
    private static HikariDataSource dataSource;

    public static HikariDataSource getDataSource() {
        if (Objects.isNull(dataSource)) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(DatabaseConfig.DATABASE_URL);
            config.setUsername(DatabaseConfig.DATABASE_USERNAME);
            config.setPassword(DatabaseConfig.DATABASE_PASSWORD);
            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }
}