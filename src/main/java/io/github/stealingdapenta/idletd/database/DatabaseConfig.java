package io.github.stealingdapenta.idletd.database;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConfig {

    private static final Dotenv dotenv = Dotenv.configure()
                                               .filename("prod.env")
                                               .load();

    public static final String DATABASE_URL = dotenv.get("DB_URL");
    public static final String DATABASE_USERNAME = dotenv.get("DB_USERNAME");
    public static final String DATABASE_PASSWORD = dotenv.get("DB_PASSWORD");
    // admin url : http://162.222.196.126/phpmyadmin

    private DatabaseConfig() {
    }
}
