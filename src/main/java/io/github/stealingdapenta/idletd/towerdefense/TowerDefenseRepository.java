package io.github.stealingdapenta.idletd.towerdefense;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static io.github.stealingdapenta.idletd.Idletd.logger;
import static io.github.stealingdapenta.idletd.database.DatabaseManager.getDataSource;

@RequiredArgsConstructor
public class TowerDefenseRepository {

    public Long insertTowerDefense(TowerDefense towerDefense) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO TOWER_DEFENSE (FK_IDLE_PLAYER, FK_PLOT, STAGE_LEVEL, WAVE_START_TIME, WAVE_ACTIVE) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, towerDefense.getPlayerUUID().toString());
            statement.setLong(2, towerDefense.getPlot());
            statement.setInt(3, towerDefense.getStageLevel());
            statement.setLong(4, towerDefense.getWaveStartTime());
            statement.setBoolean(5, towerDefense.isWaveActive());

            statement.execute();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error inserting TowerDefense: " + e.getMessage());
        }
        return null;
    }

    public TowerDefense getTowerDefense(UUID uuid) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM TOWER_DEFENSE WHERE FK_IDLE_PLAYER = ?")) {

            statement.setString(1, uuid.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return convertResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting TowerDefense by IdlePlayer ID: " + e.getMessage());
        }
        return null;
    }

    public void updateTowerDefense(TowerDefense towerDefense) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE TOWER_DEFENSE SET FK_PLOT=?, STAGE_LEVEL=?, WAVE_START_TIME=?, WAVE_ACTIVE=? WHERE FK_IDLE_PLAYER=?")) {

            prepareTowerDefenseStatement(towerDefense, statement);
            statement.setString(5, towerDefense.getPlayerUUID().toString());

            statement.executeUpdate();

        } catch (SQLException e) {
            logger.severe("Error updating TowerDefense: " + e.getMessage());
        }
    }

    public void deleteTowerDefense(long fkIdlePlayer) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM TOWER_DEFENSE WHERE FK_IDLE_PLAYER = ?")) {

            statement.setLong(1, fkIdlePlayer);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.severe("Error deleting TowerDefense: " + e.getMessage());
        }
    }

    private void prepareTowerDefenseStatement(TowerDefense towerDefense, PreparedStatement statement) throws SQLException {
        statement.setLong(1, towerDefense.getPlot());
        statement.setInt(2, towerDefense.getStageLevel());
        statement.setLong(3, towerDefense.getWaveStartTime());
        statement.setBoolean(4, towerDefense.isWaveActive());
    }

    private TowerDefense convertResultSet(ResultSet resultSet) throws SQLException {
        return TowerDefense.builder()
                           .playerUUID(UUID.fromString(resultSet.getString("FK_IDLE_PLAYER")))
                           .plot(resultSet.getLong("FK_PLOT"))
                           .stageLevel(resultSet.getInt("STAGE_LEVEL"))
                           .waveStartTime(resultSet.getLong("WAVE_START_TIME"))
                           .waveActive(resultSet.getBoolean("WAVE_ACTIVE"))
                           .build();
    }
}