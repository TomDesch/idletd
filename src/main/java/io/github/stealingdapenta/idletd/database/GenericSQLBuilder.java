package io.github.stealingdapenta.idletd.database;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GenericSQLBuilder {
    private static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS ";
    private static final String INSERT_SQL = "INSERT INTO ";
    private static final String VALUES = "VALUES ";
    private static final String OPEN = "(";
    private static final String CLOSE = ")";
    private static final String INTERROGATION = "?";

    /**
     * @param tableName              the name of the table to create.
     * @param columnsWithDefinitions the column names with their required definition
     *                               e.g. INT NOT NULL or VARCHAR(40) NOT NULL
     * @return SQL creation statement
     */
    public static String buildCreateTableSQL(String tableName, Map<String, String> columnsWithDefinitions) {
        String columns = columnsWithDefinitions.entrySet()
                                               .stream()
                                               .map(entry -> entry.getKey() + " " + entry.getValue())
                                               .collect(Collectors.joining(", "));

        return CREATE_SQL + removeWhiteSpace(tableName) + OPEN + columns + CLOSE;
    }

    public static String buildInsertSQL(String tableName, Set<String> columns) {
        String columnsString = String.join(", ", columns);
        String values = columns.stream().map(col -> INTERROGATION).collect(Collectors.joining(", "));

        return INSERT_SQL + removeWhiteSpace(tableName) + OPEN + columnsString + CLOSE + VALUES + OPEN + values + CLOSE;
    }

    private static String removeWhiteSpace(String input) {
        return input.replaceAll("\\s", "");
    }
}
