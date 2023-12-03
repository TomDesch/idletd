package io.github.stealingdapenta.idletd.agent;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static io.github.stealingdapenta.idletd.Idletd.logger;
import static io.github.stealingdapenta.idletd.agent.AgentType.getAgentTypeById;
import static io.github.stealingdapenta.idletd.database.DatabaseManager.getDataSource;

@RequiredArgsConstructor
public class AgentRepository {

    public void saveAgent(Agent agent) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO AGENT (PLAYERUUID, AGENT_TYPE, FK_LOCATION) VALUES (?, ?, ?)",
                                                                       Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, agent.getPlayerUUID().toString());
            statement.setInt(2, agent.getAgentType().getId());
            statement.setLong(3, agent.getFkLocation());

            statement.execute();
        } catch (SQLException e) {
            logger.severe("Error inserting Agent: " + e.getMessage());
        }
    }

    public Agent getAgent(int id) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM AGENT WHERE ID = ?")) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return convertResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting Agent by ID: " + e.getMessage());
        }
        return null;
    }

    public void updateAgent(Agent agent) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE AGENT SET PLAYERUUID=?, AGENT_TYPE=?, FK_LOCATION=? WHERE ID=?")) {

            statement.setString(1, agent.getPlayerUUID().toString());
            statement.setInt(2, agent.getAgentType().ordinal());
            statement.setLong(3, agent.getFkLocation());
            statement.setInt(4, agent.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error updating Agent: " + e.getMessage());
        }
    }

    public void deleteAgent(int id) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM AGENT WHERE ID = ?")) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.severe("Error deleting Agent: " + e.getMessage());
        }
    }


    private Agent convertResultSet(ResultSet resultSet) throws SQLException {
        return Agent.builder()
                    .id(resultSet.getInt("ID"))
                    .playerUUID(UUID.fromString(resultSet.getString("PLAYERUUID")))
                    .agentType(getAgentTypeById(resultSet.getInt("AGENT_TYPE")))
                    .fkLocation(resultSet.getInt("FK_LOCATION"))
                    .build();
    }
}
