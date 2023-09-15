package io.github.stealingdapenta.idletd.plot;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.service.utils.World;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.github.stealingdapenta.idletd.database.DatabaseManager.getDataSource;
import static io.github.stealingdapenta.idletd.database.DatabaseManager.insertIfNotExists;
import static io.github.stealingdapenta.idletd.service.utils.World.TOWER_DEFENSE_WORLD;

@Builder
@Data
@AllArgsConstructor
public class Plot {
    private static final String CREATE_PLOT_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS plots (" + "id INT AUTO_INCREMENT PRIMARY KEY," + "startX INT NOT NULL," + "startZ INT NOT NULL," + "playerUUID " + "VARCHAR(36) NOT NULL)";
    private static final String INSERT_PLOT_SQL = "INSERT INTO plots (startX, startZ, playerUUID) VALUES (?, ?, ?)";
    private static final String GET_LATEST_PLOT_SQL = "SELECT * FROM plots ORDER BY id DESC LIMIT 1";
    private static final String GET_PLOT_BY_UUID_SQL = "SELECT * FROM plots WHERE playerUUID = ? LIMIT 1";

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
        try (Connection connection = getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(CREATE_PLOT_TABLE_SQL)) {
            statement.execute();
        }
    }

    public static void populatePlotTable() {
        String tableName = "plots";
        String[] columns = {"startX", "startZ", "playerUUID"};
        Object[] values = {0, 0, "SERVER"};
        try {
            insertIfNotExists(tableName, columns, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertPlot(Plot plot) throws SQLException {
        try (Connection connection = getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(INSERT_PLOT_SQL)) {
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
                e.printStackTrace();
                return null;
            }
        });
    }

    public static Plot findOwnedPlot(Player player) {
        CompletableFuture<Plot> asyncPlot = asyncGetPlotByUUID(player.getUniqueId().toString());
        try {
            return asyncPlot.get(); // This blocks until the async operation completes
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace(); // Handle exceptions appropriately
            Idletd.getInstance().getLogger().warning("SOMETHINGS WRONG I CAN FEEL IT 35");
        }
        return null;
    }

    public Location getPlayerSpawnPoint() {
        double x = this.getStartX() + 200;
        double y = 90;
        double z = this.getStartZ() + 43;
        return new Location(TOWER_DEFENSE_WORLD.getBukkitWorld(), x, y, z);
    }
}