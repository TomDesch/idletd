package io.github.stealingdapenta.idletd.service.command.plot;

import io.github.stealingdapenta.idletd.plot.Plot;
import io.github.stealingdapenta.idletd.plot.PlotHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static io.github.stealingdapenta.idletd.plot.Plot.findOwnedPlot;

@RequiredArgsConstructor
public class GoToPlotCommand implements CommandExecutor {

    private final PlotHandler plotHandler;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        Plot existingPlot = findOwnedPlot(player);

        if (Objects.isNull(existingPlot)) {
            player.sendMessage("Can not find an existing plot for " + player.getUniqueId());
            player.sendMessage("Please create a plot with /plot new");
            return false;
        }

        player.teleport(existingPlot.getPlayerSpawnPoint());
        return true;
    }
}
