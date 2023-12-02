package io.github.stealingdapenta.idletd.plot;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import io.github.stealingdapenta.idletd.service.utils.SchematicHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.github.stealingdapenta.idletd.Idletd.logger;
import static io.github.stealingdapenta.idletd.service.utils.Schematic.TOWER_DEFENSE_SCHEMATIC;
import static io.github.stealingdapenta.idletd.service.utils.World.TOWER_DEFENSE_WORLD;

@RequiredArgsConstructor
public class PlotService {
    private static final int PLOT_SIZE = 500;
    private final SchematicHandler schematicHandler;
    private final PlotRepository plotRepository;
    private Plot lastGeneratedPlot = null;

    public Plot generatePlotWithTower(Player player) {
        logger.info("Commencing plot generation.");
        Plot plot = this.generatePlotForPlayer(player);

        logger.info("Started pasting structure in plot.");
        this.pasteTowerInPlot(plot);
        return plot;
    }

    public Plot getPlot(long id) {
        return this.plotRepository.getPlotById(id);
    }

    public Plot generatePlotForPlayer(Player player) {
        logger.info("Commencing plot generation for " + player.getName());

        Plot existingPlot = plotRepository.findPlot(player);
        if (Objects.nonNull(existingPlot)) {
            logger.warning(player.getName() + " already has a plot.");
            player.sendMessage("You already have a plot.");
            return existingPlot;
        }

        int lastGeneratedRow = this.getLastGeneratedRow();
        int lastGeneratedColumn = this.getLastGeneratedColumn();

        logger.info("Starting plot generation for row, column: " + lastGeneratedRow + ", " + lastGeneratedColumn);


        int currentRow = lastGeneratedRow;
        int currentColumn = lastGeneratedColumn + 1;

        if (currentColumn >= 100) {
            currentColumn = 0;
            currentRow++;
        }

        int startX = currentColumn * PLOT_SIZE;
        int startZ = currentRow * PLOT_SIZE;

        logger.info("Plot generation new x and Z: " + startX + " " + startZ);

        Plot plot = Plot.builder()
                        .startX(startX)
                        .startZ(startZ)
                        .playerUUID(player.getUniqueId())
                        .build();
        generatePlot(plot);
        PlotRepository.insertPlot(plot);
        this.lastGeneratedPlot = plot;

        logger.info("Finishing plot generation.");
        return plot;
    }

    public void pasteTowerInPlot(Plot plot) {
        Location pasteLocation = plot.calculateTowerLocation();
        logger.info("Commencing pasting new structure for plot ID: " + plot.getId());
        this.schematicHandler.pasteSchematic(TOWER_DEFENSE_SCHEMATIC.getFileName(), pasteLocation);
    }

    public Plot findPlot(Player player) {
        CompletableFuture<Plot> asyncPlot = this.plotRepository.asyncGetPlotByUUID(player.getUniqueId().toString());
        try {
            return asyncPlot.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            logger.warning("Error finding plot for player.");
        }
        return null;
    }

    public int getLastGeneratedRow() {
        Plot plot = getLastGeneratedPlot();
        if (plot != null) {
            return plot.getStartZ() / PLOT_SIZE;
        }
        logger.warning("Error fetching last generated plot row.");
        return -1;
    }

    public int getLastGeneratedColumn() {
        Plot plot = getLastGeneratedPlot();
        if (plot != null) {
            return plot.getStartX() / PLOT_SIZE;
        }
        logger.warning("Error fetching last generated plot column.");
        return -1;
    }


    public Plot getLastGeneratedPlot() {
        if (lastGeneratedPlot == null) {
            CompletableFuture<Plot> asyncPlot = plotRepository.asyncGetLatestPlot();
            try {
                lastGeneratedPlot = asyncPlot.get(); // This blocks until the async operation completes
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace(); // Handle exceptions appropriately
                logger.warning("Error getting last generated plot.");
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

        logger.info("Generating plot at (X, Y, Z): (" + minPointX + ", Y, " + minPointZ + ").");

        ProtectedCuboidRegion region = new ProtectedCuboidRegion(plot.getPlayerUUID(),
                                                                 BlockVector3.at(minPointX, bukkitWorld.getMinHeight(), minPointZ),
                                                                 BlockVector3.at(maxPointX, bukkitWorld.getMaxHeight(), maxPointZ));

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
