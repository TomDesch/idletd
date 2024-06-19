package io.github.stealingdapenta.idletd.database;

import static io.github.stealingdapenta.idletd.Idletd.LOGGER;
import static io.github.stealingdapenta.idletd.database.DatabaseConfig.DATABASE_URL;
import static io.github.stealingdapenta.idletd.database.DatabaseConfig.DATABASE_USERNAME;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Objects;


public class DatabaseManager {
    private static HikariDataSource dataSource;

    public static HikariDataSource getDataSource() {
        if (Objects.isNull(dataSource)) {
            HikariConfig config = new HikariConfig();
            LOGGER.info("Setting with url %s, user %s".formatted(DATABASE_URL, DATABASE_USERNAME));
            config.setJdbcUrl(DATABASE_URL);
            config.setUsername(DatabaseConfig.DATABASE_USERNAME);
            config.setPassword(DatabaseConfig.DATABASE_PASSWORD);
            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }
}