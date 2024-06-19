package io.github.stealingdapenta.idletd.agent;

import static io.github.stealingdapenta.idletd.Idletd.LOGGER;
import static io.github.stealingdapenta.idletd.agent.AgentType.getAgentTypeById;
import static io.github.stealingdapenta.idletd.database.DatabaseManager.getDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AgentRepository {

    public long saveAgent(Agent agent) {
        try (Connection connection = getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO AGENT (PLAYERUUID, AGENT_TYPE, FK_LOCATION, ACTIVE_SKIN_ID) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, agent.getPlayerUUID().toString());
            statement.setInt(2, agent.getAgentType().getTypeId());
            statement.setLong(3, agent.getFkLocation());
            statement.setInt(4, agent.getActiveSkinId());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating Agent failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error inserting Agent: " + e.getMessage());
            return -1;
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
            LOGGER.severe("Error getting Agent by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Agent> getAgentsByPlayerUUID(UUID playerUUID) {
        List<Agent> agentList = new ArrayList<>();

        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM AGENT WHERE PLAYERUUID = ?")) {

            statement.setString(1, playerUUID.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    agentList.add(convertResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error getting Agents by PLAYERUUID: " + e.getMessage());
        }

        return agentList;
    }


    public void updateAgent(Agent agent) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE AGENT SET PLAYERUUID=?, AGENT_TYPE=?, FK_LOCATION=?, ACTIVE_SKIN_ID=? WHERE ID=?")) {

            statement.setString(1, agent.getPlayerUUID().toString());
            statement.setInt(2, agent.getAgentType().getTypeId());
            statement.setLong(3, agent.getFkLocation());
            statement.setInt(4, agent.getActiveSkinId());
            statement.setInt(5, agent.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("Error updating Agent: " + e.getMessage());
        }
    }

    public void deleteAgent(int id) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM AGENT WHERE ID = ?")) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.severe("Error deleting Agent: " + e.getMessage());
        }
    }


    private Agent convertResultSet(ResultSet resultSet) throws SQLException {
        return Agent.builder()
                    .id(resultSet.getInt("ID"))
                    .playerUUID(UUID.fromString(resultSet.getString("PLAYERUUID")))
                    .agentType(getAgentTypeById(resultSet.getInt("AGENT_TYPE")))
                    .fkLocation(resultSet.getInt("FK_LOCATION"))
                    .activeSkinId(resultSet.getInt("ACTIVE_SKIN_ID"))
                    .build();
    }
}
