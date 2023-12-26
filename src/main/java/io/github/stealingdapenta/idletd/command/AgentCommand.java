package io.github.stealingdapenta.idletd.command;

import io.github.stealingdapenta.idletd.agent.Agent;
import io.github.stealingdapenta.idletd.agent.AgentManager;
import io.github.stealingdapenta.idletd.agent.AgentService;
import io.github.stealingdapenta.idletd.agent.AgentType;
import io.github.stealingdapenta.idletd.agent.mainagent.MainAgentStats;
import io.github.stealingdapenta.idletd.agent.mainagent.MainAgentStatsRepository;
import io.github.stealingdapenta.idletd.agent.mainagent.MainAgentStatsService;
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
    private final MainAgentStatsService mainAgentStatsService;

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



            long agentId = agentService.saveAgent(agent);
            MainAgentStats mainAgentStats = MainAgentStats.builder()
                                                          .agentId((int) agentId)
                                                          .maxHealth(10.0)
                                                          .regenerationPerSecond(0)
                                                          .overhealShieldLimit(0)
                                                          .overhealShieldRegenerationPerSecond(0)
                                                          .swordResistance(1.0)
                                                          .axeResistance(1.0)
                                                          .magicResistance(1.0)
                                                          .arrowResistance(1.0)
                                                          .tridentResistance(1.0)
                                                          .explosionResistance(1.0)
                                                          .fireResistance(1.0)
                                                          .poisonResistance(1.0)
                                                          .criticalHitResistance(1.0)
                                                          .blockChance(1.0)
                                                          .attackPower(1.0)
                                                          .attackRange(1.0)
                                                          .attackKnockback(1.0)
                                                          .attackSpeed(1.0)
                                                          .projectileSpeed(1.0)
                                                          .criticalHitChance(1.0)
                                                          .criticalHitDamageMultiplier(1.0)
                                                          .build();

            mainAgentStatsService.saveMainAgentStats(mainAgentStats);
        }

        agentManager.activateAllInactiveAgents(idlePlayer);
        return true;
    }
}
