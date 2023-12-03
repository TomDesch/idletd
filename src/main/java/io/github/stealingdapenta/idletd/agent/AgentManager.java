package io.github.stealingdapenta.idletd.agent;

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
    private final AgentService agentService;

    public boolean activateAgent(Agent agent) {
        // Summon npc todo
        return activeAgents.add(agent);
    }

    public boolean deactivateAgent(Agent agent) {
        // Desummon npc todo
        return activeAgents.remove(agent);
    }

    public void activateAllAgents(IdlePlayer idlePlayer) {
        List<Agent> agents = agentService.findAllForPlayer(idlePlayer);
        if (Objects.isNull(agents) || agents.isEmpty()) {
            logger.info("No agents found in DB for " + idlePlayer.getPlayerUUID().toString());
            return;
        }

        agents.forEach(this::activateAgent);
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
        if (Objects.isNull(agent.getId())) {
            agentService.saveAgent(agent);
        } else {
            agentService.updateAgent(agent);
        }
        deactivateAgent(agent);
    }
}
