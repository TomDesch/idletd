package io.github.stealingdapenta.idletd.command.plot;

import io.github.stealingdapenta.idletd.plot.Plot;
import io.github.stealingdapenta.idletd.plot.PlotService;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static io.github.stealingdapenta.idletd.Idletd.logger;


@RequiredArgsConstructor
public class CreatePlotCommand implements CommandExecutor {

    private final PlotService plotService;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        Plot existingPlot = this.plotService.findPlot(player);

        if (Objects.nonNull(existingPlot)) {
            logger.info(player.getName() + " already has a plot.");
            player.sendMessage("You already have an existing plot. ID:" + existingPlot.getId());
            return true;
        }

        Plot plot = this.plotService.generatePlotWithTower(player);

        player.sendMessage("Teleporting you to your new plot.");
        player.teleport(plot.getPlayerSpawnPoint());
        return true;
    }
}

