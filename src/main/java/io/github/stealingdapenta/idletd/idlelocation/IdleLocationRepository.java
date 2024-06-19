package io.github.stealingdapenta.idletd.idlelocation;

import static io.github.stealingdapenta.idletd.Idletd.LOGGER;
import static io.github.stealingdapenta.idletd.database.DatabaseManager.getDataSource;
import static io.github.stealingdapenta.idletd.utils.ANSIColor.RED;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IdleLocationRepository {
    public int saveIdleLocation(IdleLocation idleLocation) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO LOCATION (WORLD_NAME, LOCATION_X, LOCATION_Y, LOCATION_Z, LOCATION_YAW, LOCATION_PITCH) VALUES (?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            prepareIdleLocationStatement(idleLocation, statement);
            statement.execute();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to get ID for the saved IdleLocation.");
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(RED, "Error saving IdleLocation: " + e.getMessage());
            return -1; // or throw a specific exception based on your error handling strategy
        }
    }


    public IdleLocation getIdleLocation(int id) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM LOCATION WHERE ID = ?")) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return convertResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(RED, "Error getting IdleLocation by ID: " + e.getMessage());
        }
        return null;
    }

    public void updateIdleLocation(IdleLocation idleLocation) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE LOCATION SET WORLD_NAME=?, LOCATION_X=?, LOCATION_Y=?, LOCATION_Z=?, LOCATION_YAW=?, LOCATION_PITCH=? WHERE ID=?")) {

            prepareIdleLocationStatement(idleLocation, statement);
            statement.setInt(7, idleLocation.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.severe(RED, "Error updating IdleLocation: " + e.getMessage());
        }
    }

    public void deleteIdleLocation(int id) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM LOCATION WHERE ID = ?")) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.severe(RED, "Error deleting IdleLocation: " + e.getMessage());
        }
    }

    private void prepareIdleLocationStatement(IdleLocation idleLocation, PreparedStatement statement) throws SQLException {
        statement.setString(1, idleLocation.getWorldName());
        statement.setDouble(2, idleLocation.getX());
        statement.setDouble(3, idleLocation.getY());
        statement.setDouble(4, idleLocation.getZ());
        statement.setFloat(5, idleLocation.getYaw());
        statement.setFloat(6, idleLocation.getPitch());
    }

    private IdleLocation convertResultSet(ResultSet resultSet) throws SQLException {
        return IdleLocation.builder()
                           .id(resultSet.getInt("ID"))
                           .worldName(resultSet.getString("WORLD_NAME"))
                           .x(resultSet.getDouble("LOCATION_X"))
                           .y(resultSet.getDouble("LOCATION_Y"))
                           .z(resultSet.getDouble("LOCATION_Z"))
                           .yaw(resultSet.getFloat("LOCATION_YAW"))
                           .pitch(resultSet.getFloat("LOCATION_PITCH"))
                           .build();
    }
}

