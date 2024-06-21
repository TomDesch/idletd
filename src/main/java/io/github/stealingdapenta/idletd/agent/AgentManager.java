package io.github.stealingdapenta.idletd.agent;

import static io.github.stealingdapenta.idletd.Idletd.LOGGER;

import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;


@RequiredArgsConstructor
public class AgentManager {

    private static final Set<Agent> activeAgents = new HashSet<>();
    private final AgentService agentService;
    private final AgentStatsService agentStatsService;

    public Agent getAgentMatchingNPC(Entity entity) {
        return activeAgents
                .stream()
                .filter(agent -> agent.getAgentNPC()
                                      .getNpc()
                                      .getEntity()
                                      .getUniqueId()
                                      .equals(entity.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    public Agent getActiveMainAgent(IdlePlayer idlePlayer) {
        return getAllActiveAgents(idlePlayer)
                .stream()
                .filter(agent -> agent.getAgentType()
                                      .equals(AgentType.MAIN_AGENT))
                .findFirst()
                .orElse(null);
    }

    public boolean hasActiveAgents(IdlePlayer idlePlayer) {
        List<Agent> agents = getAllActiveAgents(idlePlayer);
        return Objects.nonNull(agents) && !agents.isEmpty();
    }

    private boolean isActive(Agent agent) {
        return activeAgents.contains(agent);
    }

    private boolean isInactive(Agent agent) {
        return !isActive(agent);
    }

    public void activateAllInactiveAgents(IdlePlayer idlePlayer) {
        List<Agent> agents = agentService.findAllForPlayer(idlePlayer);
        if (Objects.isNull(agents) || agents.isEmpty()) {
            return;
        }
        agents = agents.stream()
                       .filter(this::isInactive)
                       .toList();
        agents.forEach(this::activate);
    }

    public void activate(Agent agent) {
        agentService.summonNPC(agent);

        activeAgents.add(agent);
    }

    public boolean deactivate(Agent agent) {
        agentService.despawnAndDestroyNPC(agent);
        return activeAgents.remove(agent);
    }

    private List<Agent> getAllActiveAgents(IdlePlayer idlePlayer) {
        return activeAgents.stream()
                           .filter(agent -> agentBelongsToPlayer(agent, idlePlayer))
                           .toList();
    }

    private boolean agentBelongsToPlayer(Agent agent, IdlePlayer idlePlayer) {
        return Objects.nonNull(agent.getPlayerUUID()) && agent.getPlayerUUID()
                                                              .equals(idlePlayer.getPlayerUUID());
    }

    public void deactivateAndSaveAllAgents(IdlePlayer idlePlayer) {
        List<Agent> activeAgentsForPlayer = getAllActiveAgents(idlePlayer);
        if (Objects.isNull(activeAgentsForPlayer)) {
            LOGGER.info("No active agents found for " + idlePlayer.getPlayerUUID()
                                                                  .toString());
            return;
        }

        activeAgentsForPlayer.forEach(this::saveAndDeactivate);
    }

    public void saveAndDeactivate(Agent agent) {
        if (agent.getId() == 0) {
            agentService.saveAgent(agent);
            agentStatsService.saveAgentStats(agent.getFetchedAgentStats());
        } else {
            agentService.updateAgent(agent);
            agentStatsService.updateAgentStats(agent.getFetchedAgentStats());
        }
        deactivate(agent);
    }
}
