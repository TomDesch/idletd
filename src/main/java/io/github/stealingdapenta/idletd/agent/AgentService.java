package io.github.stealingdapenta.idletd.agent;

import io.github.stealingdapenta.idletd.agent.npc.AgentNPC;
import io.github.stealingdapenta.idletd.idlelocation.IdleLocationService;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.service.utils.EntityTracker;
import io.github.stealingdapenta.idletd.skin.Skin;
import io.github.stealingdapenta.idletd.skin.SkinService;
import lombok.RequiredArgsConstructor;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
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
        fetchFields(agent);
        return agent;
    }

    public void despawnAndDestroyNPC(Agent agent) {
        NPC npc = agent.getAgentNPC().getNpc();
        if (Objects.nonNull(npc)) {
            agent.getAgentNPC().getNpc().despawn();
            CitizensAPI.getNPCRegistry().deregister(npc);
        }
        agent.getAgentNPC().setNpc(null);
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
                               .currentSkin(agent.getFetchedSkin())
                               .target(owner)
                               .name(agent.getAgentType().getName())
                               .levelHealth(100)
                               .build();
            agent.setAgentNPC(agentNPC);
        }

        agentNPC.setCurrentSkin(agent.getFetchedSkin());
        agentNPC.setLocation(agent.getFetchedLocation());
        agentNPC.spawn();
        agentNPC.updateTarget();
        return agentNPC;
    }

    private void targetterTask(Agent agent) {
        // todo (one task for all agents pls) <= in Manager!
    }

    public List<Agent> findAllForPlayer(IdlePlayer idlePlayer) {
        List<Agent> agents = findAllForPlayer(idlePlayer.getPlayerUUID());
        if (Objects.isNull(agents) || agents.isEmpty()) {
            logger.info("No agents found in DB for " + idlePlayer.getPlayerUUID().toString());
            return agents;
        }

        logger.info("Amount of agents found: " + agents.size());
        agents.forEach(this::fetchFields);
        return agents;
    }

    private List<Agent> findAllForPlayer(UUID uuid) {
        List<Agent> agents = agentRepository.getAgentsByPlayerUUID(uuid);
        if (Objects.isNull(agents)) {
            return null;
        }
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
        fetchSkinIfNull(agent);
    }

    private Skin fetchSkinIfNull(Agent agent) {
        if (Objects.isNull(agent.getFetchedSkin())) {
            agent.setFetchedSkin(skinService.getSkin(agent.getActiveSkinId()));
        }
        return agent.getFetchedSkin();
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
