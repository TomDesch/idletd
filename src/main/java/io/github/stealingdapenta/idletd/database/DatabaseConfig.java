package io.github.stealingdapenta.idletd.database;

public class DatabaseConfig {

    public static final String DATABASE_URL = System.getenv("DB_URL");
    public static final String DATABASE_USERNAME = System.getenv("DB_USERNAME");
    public static final String DATABASE_PASSWORD = System.getenv("DB_PASSWORD");
    // admin url : http://162.222.196.126/phpmyadmin

    private DatabaseConfig() {
    }
}
