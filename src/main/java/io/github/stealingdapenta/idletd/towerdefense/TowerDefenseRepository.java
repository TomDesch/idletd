package io.github.stealingdapenta.idletd.towerdefense;

import static io.github.stealingdapenta.idletd.Idletd.LOGGER;
import static io.github.stealingdapenta.idletd.database.DatabaseManager.getDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TowerDefenseRepository {

    public void insertTowerDefense(TowerDefense towerDefense) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO TOWER_DEFENSE (PLAYERUUID, FK_PLOT, STAGE_LEVEL) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, towerDefense.getPlayerUUID().toString());
            statement.setLong(2, towerDefense.getPlot());
            statement.setInt(3, towerDefense.getStageLevel());

            statement.execute();
        } catch (SQLException e) {
            LOGGER.severe("Error inserting TowerDefense: " + e.getMessage());
        }
    }

    public TowerDefense getTowerDefense(UUID uuid) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM TOWER_DEFENSE WHERE PLAYERUUID = ?")) {

            statement.setString(1, uuid.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return convertResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error getting TowerDefense by IdlePlayer ID: " + e.getMessage());
        }
        return null;
    }

    public void updateTowerDefense(TowerDefense towerDefense) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE TOWER_DEFENSE SET FK_PLOT=?, STAGE_LEVEL=? WHERE PLAYERUUID=?")) {

            prepareTowerDefenseStatement(towerDefense, statement);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.severe("Error updating TowerDefense: " + e.getMessage());
        }
    }

    public void deleteTowerDefense(long fkIdlePlayer) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM TOWER_DEFENSE WHERE PLAYERUUID = ?")) {

            statement.setLong(1, fkIdlePlayer);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.severe("Error deleting TowerDefense: " + e.getMessage());
        }
    }

    private void prepareTowerDefenseStatement(TowerDefense towerDefense, PreparedStatement statement) throws SQLException {
        statement.setLong(1, towerDefense.getPlot());
        statement.setInt(2, towerDefense.getStageLevel());
        statement.setString(3, towerDefense.getPlayerUUID().toString());

    }

    private TowerDefense convertResultSet(ResultSet resultSet) throws SQLException {
        return TowerDefense.builder()
                           .playerUUID(UUID.fromString(resultSet.getString("PLAYERUUID")))
                           .plot(resultSet.getLong("FK_PLOT"))
                           .stageLevel(resultSet.getInt("STAGE_LEVEL"))
                           .build();
    }
}