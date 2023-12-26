package io.github.stealingdapenta.idletd.agent;

import io.github.stealingdapenta.idletd.agent.mainagent.MainAgentStatsService;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.github.stealingdapenta.idletd.Idletd.logger;


@RequiredArgsConstructor
public class AgentManager {

    private static final Set<Agent> activeAgents = new HashSet<>();
    private static final Set<AgentStats> activeAgentsStats = new HashSet<>(); // todo use
    private final AgentService agentService;
    private final AgentStatsService agentStatsService; // todo use

    public Agent getActiveMainAgent(IdlePlayer idlePlayer) {
        return getAllActiveAgents(idlePlayer)
                .stream()
                .filter(agent -> agent.getAgentType().equals(AgentType.MAIN_AGENT))
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
        agents = agents.stream().filter(this::isInactive).toList();
        agents.forEach(this::activateAgent);
    }

    public boolean activateAgent(Agent agent) {
        agentService.summonNPC(agent);

        return activeAgents.add(agent);
    }

    public boolean deactivateAgent(Agent agent) {
        agentService.despawnAndDestroyNPC(agent);
        return activeAgents.remove(agent);
    }

    private List<Agent> getAllActiveAgents(IdlePlayer idlePlayer) {
        return activeAgents.stream()
                           .filter(agent -> agentBelongsToPlayer(agent, idlePlayer))
                           .toList();
    }

    private boolean agentBelongsToPlayer(Agent agent, IdlePlayer idlePlayer) {
        return Objects.nonNull(agent.getPlayerUUID()) && agent.getPlayerUUID().equals(idlePlayer.getPlayerUUID());
    }

    public void deactivateAndSaveAllAgents(IdlePlayer idlePlayer) {
        List<Agent> activeAgentsForPlayer = getAllActiveAgents(idlePlayer);
        if (Objects.isNull(activeAgentsForPlayer)) {
            logger.info("No active agents found for " + idlePlayer.getPlayerUUID().toString());
            return;
        }

        activeAgentsForPlayer.forEach(this::deactivateAndSaveAgent);
    }

    public void deactivateAndSaveAgent(Agent agent) {
        if (agent.getId() == 0) {
            agentService.saveAgent(agent);
        } else {
            agentService.updateAgent(agent);
        }
        deactivateAgent(agent);
    }
}
