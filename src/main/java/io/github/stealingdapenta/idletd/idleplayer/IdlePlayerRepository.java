package io.github.stealingdapenta.idletd.idleplayer;

import io.github.stealingdapenta.idletd.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.UUID;

import static io.github.stealingdapenta.idletd.Idletd.logger;

public class IdlePlayerRepository {

    public void saveIdlePlayer(IdlePlayer idlePlayer) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO IDLE_PLAYER (PLAYERUUID, BALANCE, FK_PLOT) VALUES (?, ?, ?)")) {

            statement.setString(1, idlePlayer.getPlayerUUID().toString());
            statement.setDouble(2, idlePlayer.getBalance());
            if (Objects.isNull(idlePlayer.getFkPlot())) {
                statement.setNull(3, Types.BIGINT);
            } else {
                statement.setLong(3, idlePlayer.getFkPlot());
            }

            statement.execute();
        } catch (SQLException e) {
            logger.severe("Error inserting IdlePlayer. " + idlePlayer.getPlayerUUID());
            e.printStackTrace();
        }
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
             PreparedStatement statement = connection.prepareStatement("UPDATE IDLE_PLAYER SET BALANCE=?, FK_PLOT=? WHERE PLAYERUUID=?")) {

            statement.setDouble(1, idlePlayer.getBalance());
            Long fkPlot = idlePlayer.getFkPlot(); // Long = null pointer safe; long isn't
            statement.setLong(2, fkPlot);
            statement.setString(3, idlePlayer.getPlayerUUID().toString());

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
