package io.github.stealingdapenta.idletd.idleplayer;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class IdlePlayerRepository {

    private static final Logger logger = Idletd.getInstance().getLogger();

    public static void insertIdlePlayer(IdlePlayer idlePlayer) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
//                     "INSERT INTO idle_players (PLAYERUUID, BALANCE, FK_PLOT, FK_ACHIEVEMENTS, FK_STATISTICS, FK_PERMISSIONS, FK_SKINS, FK_SETTINGS, FK_RANKS) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?,
//                     ?)",
                     "INSERT INTO idle_players (PLAYERUUID, BALANCE, FK_PLOT) " + "VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            prepareIdlePlayerStatement(idlePlayer, statement);

            statement.execute();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idlePlayer.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to retrieve generated keys.");
                }
            }
        } catch (SQLException e) {
            logger.severe("Error inserting IdlePlayer.");
            e.printStackTrace();
        }
    }

    private static void prepareIdlePlayerStatement(IdlePlayer idlePlayer, PreparedStatement statement) throws SQLException {
        statement.setString(1, idlePlayer.getPlayerUUID());
        statement.setDouble(2, idlePlayer.getBalance());
        statement.setLong(3, idlePlayer.getFkPlot());
//        statement.setLong(4, idlePlayer.getFkAchievements());
//        statement.setLong(5, idlePlayer.getFkStatistics());
//        statement.setLong(6, idlePlayer.getFkPermissions());
//        statement.setLong(7, idlePlayer.getFkSkins());
//        statement.setLong(8, idlePlayer.getFkSettings());
//        statement.setLong(9, idlePlayer.getFkRanks());
    }

    public IdlePlayer getIdlePlayer(int playerId) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM idle_players WHERE ID = ?")) {

            statement.setInt(1, playerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return convertResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting IdlePlayer by ID.");
            e.printStackTrace();
        }
        return null;
    }

    public void updateIdlePlayer(IdlePlayer idlePlayer) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
//                     "UPDATE idle_players SET PLAYERUUID=?, BALANCE=?, FK_PLOT=?, FK_ACHIEVEMENTS=?, " +
//                             "FK_STATISTICS=?, FK_PERMISSIONS=?, FK_SKINS=?, FK_SETTINGS=?, FK_RANKS=? WHERE ID=?")) {
                     "UPDATE idle_players SET PLAYERUUID=?, BALANCE=?, FK_PLOT=? WHERE ID=?")) {

            prepareIdlePlayerStatement(idlePlayer, statement);
            statement.setInt(10, idlePlayer.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            logger.severe("Error updating IdlePlayer.");
            e.printStackTrace();
        }
    }

    public IdlePlayer findIdlePlayerByUUID(String playerUUID) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM idle_players WHERE PLAYERUUID = ?")) {

            statement.setString(1, playerUUID);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return convertResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting IdlePlayer by UUID.");
            e.printStackTrace();
        }
        return null;
    }


    public void deleteIdlePlayer(int playerId) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM idle_players WHERE ID = ?")) {

            statement.setInt(1, playerId);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.severe("Error deleting IdlePlayer.");
            e.printStackTrace();
        }
    }

    private IdlePlayer convertResultSet(ResultSet resultSet) throws SQLException {
        return IdlePlayer.builder()
                         .id(resultSet.getInt("ID"))
                         .playerUUID(resultSet.getString("PLAYERUUID"))
                         .balance(resultSet.getDouble("BALANCE"))
                         .fkPlot(resultSet.getLong("FK_PLOT"))
//                         .fkAchievements(resultSet.getLong("FK_ACHIEVEMENTS"))
//                         .fkStatistics(resultSet.getLong("FK_STATISTICS"))
//                         .fkPermissions(resultSet.getLong("FK_PERMISSIONS"))
//                         .fkSkins(resultSet.getLong("FK_SKINS"))
//                         .fkSettings(resultSet.getLong("FK_SETTINGS"))
//                         .fkRanks(resultSet.getLong("FK_RANKS"))
                         .build();
    }
}
