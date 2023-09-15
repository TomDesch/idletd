package io.github.stealingdapenta.idletd.service.command.plot;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.plot.Plot;
import io.github.stealingdapenta.idletd.plot.PlotHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.github.stealingdapenta.idletd.plot.Plot.asyncGetPlotByUUID;

@RequiredArgsConstructor
public class CreatePlotCommand implements CommandExecutor {

    private final PlotHandler plotHandler;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        Plot existingPlot = this.findOwnedPlot(player);
        if (Objects.nonNull(existingPlot)) {
            Idletd.getInstance().getLogger().warning(player.getName() + " already has a plot.");

            player.sendMessage("found existing plot. Not pasting new structure. " + existingPlot.getStartX());  // todo remove
            return false;
        }

        Idletd.getInstance().getLogger().info("Commencing plot generation.");
        Plot plot = this.plotHandler.generatePlotForPlayer(player);


        Idletd.getInstance().getLogger().info("Plot generation ended.");
        Idletd.getInstance().getLogger().info("Started pasting:");
        this.plotHandler.pasteTowerInPlot(plot);
        Idletd.getInstance().getLogger().info("Ended pasting");

        player.sendMessage("Teleporting you to your new plot.");
        player.teleport(plot.getPlayerSpawnPoint());
        // todo tp player to plot & set specific plot points e.g. spawn area
        return true;
    }


    private Plot findOwnedPlot(Player player) {
        CompletableFuture<Plot> asyncPlot = asyncGetPlotByUUID(player.getUniqueId().toString());
        try {
            return asyncPlot.get(); // This blocks until the async operation completes
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace(); // Handle exceptions appropriately
            Idletd.getInstance().getLogger().warning("SOMETHINGS WRONG I CAN FEEL IT 35");
        }
        return null;
    }

}

