package io.github.stealingdapenta.idletd.plot;

import io.github.stealingdapenta.idletd.Idletd;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import static io.github.stealingdapenta.idletd.database.DatabaseManager.getDataSource;
import static io.github.stealingdapenta.idletd.database.DatabaseManager.insertIfNotExists;
import static io.github.stealingdapenta.idletd.database.GenericSQLBuilder.buildCreateTableSQL;
import static io.github.stealingdapenta.idletd.database.GenericSQLBuilder.buildInsertSQL;
import static io.github.stealingdapenta.idletd.service.utils.World.TOWER_DEFENSE_WORLD;

@Builder
@Data
@AllArgsConstructor
public class Plot {
    private static final String TABLE_NAME = "plot";
    private static final String GET_LATEST_PLOT_SQL = "SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC LIMIT 1";
    private static final String GET_PLOT_BY_UUID_SQL = "SELECT * FROM " + TABLE_NAME + " WHERE playerUUID = ? LIMIT 1";
    private static final Logger logger = Idletd.getInstance().getLogger();


    private static final Map<String, String> TABLE_DEFINITION = Map.of(
            "id", "INT AUTO_INCREMENT PRIMARY KEY",
            "startX", "INT NOT NULL",
            "startZ", "INT NOT NULL",
            "playerUUID", "VARCHAR(36) NOT NULL");


    private int id;
    private int startX;
    private int startZ;
    private String playerUUID;

    public Plot(int startX, int startZ, UUID playerUUID) {
        this.startX = startX;
        this.startZ = startZ;
        this.playerUUID = playerUUID.toString();
    }

    public static void createPlotTable() throws SQLException {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(buildCreateTableSQL(TABLE_NAME, TABLE_DEFINITION))) {
            statement.execute();
        }
    }

    public static void populatePlotTable(int startX, int startZ, String playerUUID) {
        String[] columns = {"startX", "startZ", "playerUUID"};
        Object[] values = {startX, startZ, playerUUID};
        try {
            insertIfNotExists(TABLE_NAME, columns, values);
        } catch (SQLException e) {
            logger.warning("Error executing populate Plot table.");
            e.printStackTrace();
        }
    }

    public static void insertPlot(Plot plot) throws SQLException {
        Set<String> insertColumns = Set.of("startX", "startZ", "playerUUID");

        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(buildInsertSQL(TABLE_NAME, insertColumns))) {
            statement.setInt(1, plot.getStartX());
            statement.setInt(2, plot.getStartZ());
            statement.setString(3, plot.getPlayerUUID());
            statement.execute();
        }
    }

    public static Plot getLatestPlot() throws SQLException {
        try (Connection connection = getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_LATEST_PLOT_SQL); ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return new Plot(resultSet.getInt("id"), resultSet.getInt("startX"), resultSet.getInt("startZ"), resultSet.getString("playerUUID"));
            }
            return null;
        }
    }

    public static CompletableFuture<Plot> asyncGetLatestPlot() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getLatestPlot();
            } catch (SQLException e) {
                logger.warning("Error executing asyncGetLatestPlot.");
                e.printStackTrace();
                return null;
            }
        });
    }

    public static Plot getPlotByUUID(String playerUUID) throws SQLException {
        try (Connection connection = getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_PLOT_BY_UUID_SQL)) {
            statement.setString(1, playerUUID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Plot(resultSet.getInt("id"), resultSet.getInt("startX"), resultSet.getInt("startZ"), resultSet.getString("playerUUID"));
            }
            return null;
        }
    }

    public static CompletableFuture<Plot> asyncGetPlotByUUID(String playerUUID) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getPlotByUUID(playerUUID);
            } catch (SQLException e) {
                logger.warning("Error executing asyncGetPlotByUUID.");
                e.printStackTrace();
                return null;
            }
        });
    }

    public static Plot findOwnedPlot(Player player) {
        CompletableFuture<Plot> asyncPlot = asyncGetPlotByUUID(player.getUniqueId().toString());
        try {
            return asyncPlot.get();
        } catch (ExecutionException | InterruptedException e) {
            logger.warning("Error executing findOwnedPlot.");
            e.printStackTrace();
            return null;
        }
    }

    public Location getPlayerSpawnPoint() {
        double x = this.getStartX() + 200;
        double y = 90;
        double z = this.getStartZ() + 43;
        return new Location(TOWER_DEFENSE_WORLD.getBukkitWorld(), x, y, z);
    }
}