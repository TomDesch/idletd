package io.github.stealingdapenta.idletd.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static io.github.stealingdapenta.idletd.plot.Plot.createPlotTable;
import static io.github.stealingdapenta.idletd.plot.Plot.populatePlotTable;

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

    public static void createTables() throws SQLException {
        createPlotTable();
    }

    public static void populateTables() throws SQLException {
        populatePlotTable();
    }

    public static void insertIfNotExists(String tableName, String[] columns, Object[] values) throws SQLException {
        if (columns.length != values.length || columns.length == 0) {
            throw new IllegalArgumentException("Columns and values must have the same length and cannot be empty.");
        }

        StringBuilder insertSqlBuilder = new StringBuilder("INSERT INTO ");
        insertSqlBuilder.append(tableName).append(" (");

        for (int i = 0; i < columns.length; i++) {
            insertSqlBuilder.append(columns[i]);
            if (i < columns.length - 1) {
                insertSqlBuilder.append(", ");
            }
        }

        insertSqlBuilder.append(") SELECT ");
        for (int i = 0; i < values.length; i++) {
            insertSqlBuilder.append("?");
            if (i < values.length - 1) {
                insertSqlBuilder.append(", ");
            }
        }

        insertSqlBuilder.append(" FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ");
        insertSqlBuilder.append(tableName).append(" WHERE ");

        for (int i = 0; i < columns.length; i++) {
            insertSqlBuilder.append(columns[i]).append(" = ?");

            if (i < columns.length - 1) {
                insertSqlBuilder.append(" AND ");
            }
        }

        insertSqlBuilder.append(")");

        String insertSql = insertSqlBuilder.toString();

        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSql)) {

            int parameterIndex = 1;

            for (Object value : values) {
                statement.setObject(parameterIndex++, value);
            }

            for (Object value : values) {
                statement.setObject(parameterIndex++, value);
            }

            statement.executeUpdate();
        }
    }
}