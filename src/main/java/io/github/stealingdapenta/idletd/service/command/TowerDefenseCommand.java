package io.github.stealingdapenta.idletd.service.command;

import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.plot.Plot;
import io.github.stealingdapenta.idletd.plot.PlotService;
import io.github.stealingdapenta.idletd.towerdefense.TowerDefense;
import io.github.stealingdapenta.idletd.towerdefense.TowerDefenseService;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static io.github.stealingdapenta.idletd.Idletd.logger;


@RequiredArgsConstructor
public class TowerDefenseCommand implements CommandExecutor {
    private static final String NO_PLOT = "Please create a plot before launching a TD game!";
    private static final String NO_IDLE_PLAYER = "Internal error. Please contact a system admin.";
    private final PlotService plotService;
    private final TowerDefenseService towerDefenseService;
    private final IdlePlayerService idlePlayerService;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        IdlePlayer idlePlayer = idlePlayerService.getIdlePlayer(player);

        if (Objects.isNull(idlePlayer)) {
            logger.severe(player.getName() + " doesn't have a linked IdlePlayer.");
            player.sendMessage(Component.text(NO_IDLE_PLAYER));
            return false;
        }

        TowerDefense towerDefense = towerDefenseService.findTowerDefense(idlePlayer);

        if (Objects.isNull(towerDefense)) {
            Plot plot = plotService.findPlot(player);

            if (Objects.isNull(plot)) {
                logger.info(player.getName() + " tried to start a TD game without even owning a plot yet.");
                player.sendMessage(Component.text(NO_PLOT));
                return false;
            }


            towerDefense = TowerDefense.builder()
                                       .plot(plot.getId())
                                       .playerUUID(idlePlayer.getPlayerUUID())
                                       .stageLevel(1)
                                       .build();
        }

        towerDefenseService.startWave(towerDefense);

        // todo save the game regularly
        // todo calculate AFK income (idle)

        // todo add economy, reward per kill, bank value

        // todo voor afk gold income, misschien "highest income op een round" + "duur round" saven en dan percentage daarvan per minuut uitbetalen met zelfde verhouding
        return true;
    }
}

