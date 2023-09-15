package io.github.stealingdapenta.idletd.plot;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.service.utils.SchematicHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.github.stealingdapenta.idletd.plot.Plot.asyncGetLatestPlot;
import static io.github.stealingdapenta.idletd.plot.Plot.asyncGetPlotByUUID;
import static io.github.stealingdapenta.idletd.plot.Plot.findOwnedPlot;
import static io.github.stealingdapenta.idletd.service.utils.Schematic.TOWER_DEFENSE_SCHEMATIC;
import static io.github.stealingdapenta.idletd.service.utils.World.TOWER_DEFENSE_WORLD;

@RequiredArgsConstructor
public class PlotHandler {
    private static final int PLOT_SIZE = 500;
    private final SchematicHandler schematicHandler;
    private Plot lastGeneratedPlot = null;

    public Plot generatePlotForPlayer(Player player) {

        player.sendMessage("Starting plot generation 1"); // todo remove

        Plot existingPlot = findOwnedPlot(player);
        if (Objects.nonNull(existingPlot)) {
            Idletd.getInstance().getLogger().warning(player.getName() + " already has a plot.");
            player.sendMessage("Starting plot generation 2: found existing plot: " + existingPlot.getStartX());  // todo remove
            return existingPlot;
        }

        int lastGeneratedRow = this.getLastGeneratedRow();
        int lastGeneratedColumn = this.getLastGeneratedColumn();

        player.sendMessage("Starting plot generation 3, row, column: " + lastGeneratedRow + " " + lastGeneratedColumn); // todo remove

        int currentRow = lastGeneratedRow;
        int currentColumn = lastGeneratedColumn + 1;

        if (currentColumn >= 100) {
            currentColumn = 0;
            currentRow++;
        }

        int startX = currentColumn * PLOT_SIZE;
        int startZ = currentRow * PLOT_SIZE;

        player.sendMessage("Starting plot generation 27 new x and Z: " + startX + " " + startZ); // todo remove


        Plot plot = new Plot(startX, startZ, player.getUniqueId());
        generatePlot(plot);
        try {
            Plot.insertPlot(plot);
            this.lastGeneratedPlot = plot;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        player.sendMessage("Starting plot generation 100: finishing"); // todo remove

        return plot;
    }

    public void pasteTowerInPlot(Plot plot) {
        int pasteX = plot.getStartX() + 200;
        int pasteY = 80;
        int pasteZ = plot.getStartZ() + 50;
        Location pasteLocation = new Location(TOWER_DEFENSE_WORLD.getBukkitWorld(), pasteX, pasteY, pasteZ);
        Idletd.getInstance().getLogger().info("Commencing pasting new structure at (X, Y, Z): (" + pasteX + ", " + pasteY + ", " + pasteZ + ").");
        this.schematicHandler.pasteSchematic(TOWER_DEFENSE_SCHEMATIC.getFileName(), pasteLocation);
    }

    public int getLastGeneratedRow() {
        Plot plot = getLastGeneratedPlot();
        if (plot != null) {
            return plot.getStartZ() / PLOT_SIZE;
        }
        // Handle the case when the plot is null
        Idletd.getInstance().getLogger().warning("SOMETHINGS WRONG I CAN FEEL IT 2");
        return -1; // Or some other appropriate value
    }

    public int getLastGeneratedColumn() {
        Plot plot = getLastGeneratedPlot();
        if (plot != null) {
            return plot.getStartX() / PLOT_SIZE;
        }
        // Handle the case when the plot is null
        Idletd.getInstance().getLogger().warning("SOMETHINGS WRONG I CAN FEEL IT 1");
        return -1; // Or some other appropriate value
    }


    public Plot getLastGeneratedPlot() {
        if (lastGeneratedPlot == null) {
            CompletableFuture<Plot> asyncPlot = asyncGetLatestPlot();
            try {
                lastGeneratedPlot = asyncPlot.get(); // This blocks until the async operation completes
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace(); // Handle exceptions appropriately
                Idletd.getInstance().getLogger().warning("SOMETHINGS WRONG I CAN FEEL IT 3");
            }
        }
        return lastGeneratedPlot;
    }

    private void generatePlot(Plot plot) {
        com.sk89q.worldedit.world.World towerDefenseWorldSk89qWorld = TOWER_DEFENSE_WORLD.getSk89qWorld();
        World bukkitWorld = TOWER_DEFENSE_WORLD.getBukkitWorld();

        // Plot points
        int minPointX = plot.getStartX();
        int minPointZ = plot.getStartZ();
        int maxPointX = plot.getStartX() + PLOT_SIZE;
        int maxPointZ = plot.getStartZ() + PLOT_SIZE;

        Idletd.getInstance().getLogger().info("Generating plot at (X, Y, Z): (" + minPointX + ", Y, " + minPointZ + ").");

        ProtectedCuboidRegion region = new ProtectedCuboidRegion(plot.getPlayerUUID(), BlockVector3.at(minPointX, bukkitWorld.getMinHeight(), minPointZ), BlockVector3.at(maxPointX,
                                                                                                                                                                          bukkitWorld.getMaxHeight(),
                                                                                                                                                                          maxPointZ));

        // Protection flags for the plot
        region.setFlag(Flags.DAMAGE_ANIMALS, StateFlag.State.ALLOW);
        region.setFlag(Flags.INTERACT, StateFlag.State.ALLOW);
        DefaultDomain defaultDomain = new DefaultDomain();
        defaultDomain.addPlayer(plot.getPlayerUUID());
        region.setMembers(defaultDomain);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(towerDefenseWorldSk89qWorld);
        assert regions != null;
        regions.addRegion(region);
    }
}
