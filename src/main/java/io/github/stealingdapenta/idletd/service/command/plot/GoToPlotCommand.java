package io.github.stealingdapenta.idletd.service.command.plot;

import io.github.stealingdapenta.idletd.plot.Plot;
import io.github.stealingdapenta.idletd.plot.PlotService;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@RequiredArgsConstructor
public class GoToPlotCommand implements CommandExecutor {
    private final PlotService plotService;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        Plot existingPlot = plotService.findPlot(player);

        if (Objects.isNull(existingPlot)) {
            player.sendMessage("Can not find an existing plot for " + player.getName() + ". Creating new plot.");
            existingPlot = this.plotService.generatePlotWithTower(player);
        }

        player.teleport(existingPlot.getPlayerSpawnPoint());
        return true;
    }
}
