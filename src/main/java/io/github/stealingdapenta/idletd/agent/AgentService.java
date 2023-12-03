package io.github.stealingdapenta.idletd.agent;

import io.github.stealingdapenta.idletd.agent.npc.AgentNPC;
import io.github.stealingdapenta.idletd.idlelocation.IdleLocationService;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.service.utils.EntityTracker;
import io.github.stealingdapenta.idletd.skin.SkinService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static io.github.stealingdapenta.idletd.Idletd.logger;

@RequiredArgsConstructor
public class AgentService {
    private final AgentRepository agentRepository;
    private final IdlePlayerService idlePlayerService;
    private final IdleLocationService idleLocationService;
    private final SkinService skinService;
    private final EntityTracker entityTracker;

    public Agent getAgent(int id) {
        Agent agent = agentRepository.getAgent(id);
        fetchIdlePlayerIfNull(agent);
        fetchLocationIfNull(agent);
        return agent;
    }

    public AgentNPC summonNPC(Agent agent) {
        AgentNPC agentNPC = agent.getAgentNPC();
        Player owner = idlePlayerService.getPlayer(agent.getPlayerUUID());

        if (Objects.nonNull(agentNPC)) {
            if (Objects.nonNull(agentNPC.getNpc())) {
                logger.info("Attempted to summon an NPC for an agent that already has one.");
                return agent.getAgentNPC();
            }

        } else {
            agentNPC = AgentNPC.builder()
                               .location(fetchLocationIfNull(agent))
                               .currentSkin(skinService.getSkin(agent.getActiveSkinId()))
                               .target(owner)
                               .levelHealth(100)
                               .build();
            agent.setAgentNPC(agentNPC);
        }

        agentNPC.spawn();
        agentNPC.updateTarget();
        return agentNPC;
    }

    private void targetterTask(Agent agent) {
        // todo (one task for all agents pls) <= in Manager!
    }

    public List<Agent> findAllForPlayer(IdlePlayer idlePlayer) {
        return findAllForPlayer(idlePlayer.getPlayerUUID());
    }

    private List<Agent> findAllForPlayer(UUID uuid) {
        List<Agent> agents = agentRepository.getAgentsByPlayerUUID(uuid);
        agents.forEach(this::fetchFields);
        return agents;
    }

    public void saveAgent(Agent agent) {
        agentRepository.saveAgent(agent);
    }

    public void updateAgent(Agent agent) {
        agentRepository.updateAgent(agent);
    }

    public void deleteAgent(Agent agent) {
        agentRepository.deleteAgent(agent.getId());
    }

    private void fetchFields(Agent agent) {
        fetchIdlePlayerIfNull(agent);
        fetchLocationIfNull(agent);
    }

    private IdlePlayer fetchIdlePlayerIfNull(Agent agent) {
        if (Objects.isNull(agent.getFetchedPlayer())) {
            agent.setFetchedPlayer(idlePlayerService.getIdlePlayer(agent.getPlayerUUID()));
        }
        return agent.getFetchedPlayer();
    }

    private Location fetchLocationIfNull(Agent agent) {
        if (Objects.isNull(agent.getFetchedLocation())) {
            agent.setFetchedLocation(idleLocationService.getLocation(agent.getFkLocation()));
        }
        return agent.getFetchedLocation();
    }
}
