package io.github.stealingdapenta.idletd.command;

import io.github.stealingdapenta.idletd.agent.Agent;
import io.github.stealingdapenta.idletd.agent.AgentManager;
import io.github.stealingdapenta.idletd.agent.AgentService;
import io.github.stealingdapenta.idletd.agent.AgentType;
import io.github.stealingdapenta.idletd.idlelocation.IdleLocationService;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.plot.Plot;
import io.github.stealingdapenta.idletd.plot.PlotService;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

import static io.github.stealingdapenta.idletd.Idletd.logger;

@RequiredArgsConstructor
public class AgentCommand implements CommandExecutor {
    private static final String NO_IDLE_PLAYER = "Internal error. Please contact a system admin.";
    private final PlotService plotService;
    private final IdlePlayerService idlePlayerService;
    private final AgentService agentService;
    private final AgentManager agentManager;
    private final IdleLocationService idleLocationService;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        IdlePlayer idlePlayer = idlePlayerService.getIdlePlayer(player);

        if (Objects.isNull(idlePlayer)) {
            logger.severe(player.getName() + " doesn't have a linked IdlePlayer.");
            player.sendMessage(Component.text(NO_IDLE_PLAYER));
            return true;
        }

        List<Agent> agents = agentService.findAllForPlayer(idlePlayer);
        if (Objects.isNull(agents) || agents.isEmpty()) {
            player.sendMessage(Component.text("Creating your first defensive agent!"));
            Plot plot = plotService.findPlot(player);
            if (Objects.isNull(plot)) {
                player.sendMessage(Component.text("Please create a plot first!"));
                return true;
            }

            Location summonLocation = plot.getMainAgentLocation();
            int locationId = idleLocationService.save(summonLocation);

            Agent agent = Agent.builder()
                               .agentType(AgentType.MAIN_AGENT)
                               .playerUUID(idlePlayer.getPlayerUUID())
                               .fkLocation(locationId)
                               .fetchedLocation(summonLocation)
                               .fetchedPlayer(idlePlayer)
                               .activeSkinId(5)
                               .build();

            agentService.saveAgent(agent);
        }

        agentManager.activateAllInactiveAgents(idlePlayer);
        return true;
    }
}
