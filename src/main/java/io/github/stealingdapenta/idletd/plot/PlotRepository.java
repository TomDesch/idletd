package io.github.stealingdapenta.idletd.plot;

import io.github.stealingdapenta.idletd.Idletd;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import static io.github.stealingdapenta.idletd.database.DatabaseManager.getDataSource;

@RequiredArgsConstructor
public class PlotRepository {
    private static final Logger logger = Idletd.getInstance().getLogger();

    public static void insertPlot(Plot plot) {
        try (Connection connection = getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement("INSERT INTO plots (STARTX, STARTZ, PLAYERUUID) VALUES (?, ?, ?)")) {
            statement.setInt(1, plot.getStartX());
            statement.setInt(2, plot.getStartZ());
            statement.setString(3, plot.getPlayerUUID());
            statement.execute();
        } catch (SQLException e) {
            logger.severe("Error inserting plot.");
            e.printStackTrace();
        }
    }

    public Plot getPlotById(long plotId) {
        try (Connection connection = getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM plots WHERE ID = ?")) {

            statement.setLong(1, plotId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return convertResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting Plot by ID.");
            e.printStackTrace();
        }
        return null;
    }


    public Plot getLatestPlot() {
        try (Connection connection = getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM PLOT ORDER BY ID DESC LIMIT 1"); ResultSet resultSet =
                statement.executeQuery()) {
            if (resultSet.next()) {
                return convertResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.severe("Error getting latest plot.");
            e.printStackTrace();
        }
        return null;
    }

    public Plot findPlot(Player player) {
        return findPlot(player.getUniqueId().toString());
    }

    public Plot findPlot(String playerUUID) {
        try (Connection connection = getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM PLOT WHERE PLAYERUUID = ? LIMIT 1")) {
            statement.setString(1, playerUUID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return convertResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting plot by UUID.");
            e.printStackTrace();
        }
        return null;
    }

    private Plot convertResultSet(ResultSet resultSet) throws SQLException {
        return Plot.builder().id(resultSet.getLong("ID")).startX(resultSet.getInt("STARTX")).startZ(resultSet.getInt("STARTZ")).playerUUID(UUID.fromString(resultSet.getString("PLAYERUUID"))).build();
    }

    public CompletableFuture<Plot> asyncGetLatestPlot() {
        return CompletableFuture.supplyAsync(this::getLatestPlot);
    }

    public CompletableFuture<Plot> asyncGetPlotByUUID(String playerUUID) {
        return CompletableFuture.supplyAsync(() -> findPlot(playerUUID));
    }
}