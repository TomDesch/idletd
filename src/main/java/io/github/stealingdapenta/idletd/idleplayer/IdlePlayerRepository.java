package io.github.stealingdapenta.idletd.idleplayer;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class IdlePlayerRepository {

    private static final Logger logger = Idletd.getInstance().getLogger();

    public static void insertIdlePlayer(IdlePlayer idlePlayer) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO IDLE_PLAYER (PLAYERUUID, BALANCE, FK_PLOT) VALUES (?, ?, ?)")) {

            prepareIdlePlayerStatement(idlePlayer, statement);

            statement.execute();
        } catch (SQLException e) {
            logger.severe("Error inserting IdlePlayer.");
            e.printStackTrace();
        }
    }


    private static void prepareIdlePlayerStatement(IdlePlayer idlePlayer, PreparedStatement statement) throws SQLException {
        statement.setString(1, idlePlayer.getPlayerUUID().toString());
        statement.setDouble(2, idlePlayer.getBalance());
        statement.setLong(3, idlePlayer.getFkPlot());
    }

    public IdlePlayer getIdlePlayer(UUID uuid) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM IDLE_PLAYER WHERE PLAYERUUID = ?")) {

            statement.setString(1, uuid.toString());

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

    public void updateIdlePlayer(IdlePlayer idlePlayer) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE IDLE_PLAYER SET PLAYERUUID=?, BALANCE=?, FK_PLOT=? WHERE ID=?")) {

            prepareIdlePlayerStatement(idlePlayer, statement);

            statement.executeUpdate();

        } catch (SQLException e) {
            logger.severe("Error updating IdlePlayer.");
            e.printStackTrace();
        }
    }

    public void deleteIdlePlayer(UUID uuid) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM IDLE_PLAYER WHERE PLAYERUUID = ?")) {

            statement.setString(1, uuid.toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.severe("Error deleting IdlePlayer.");
            e.printStackTrace();
        }
    }

    private IdlePlayer convertResultSet(ResultSet resultSet) throws SQLException {
        return IdlePlayer.builder()
                         .playerUUID(UUID.fromString(resultSet.getString("PLAYERUUID")))
                         .balance(resultSet.getDouble("BALANCE"))
                         .fkPlot(resultSet.getLong("FK_PLOT"))
                         .build();
    }
}
