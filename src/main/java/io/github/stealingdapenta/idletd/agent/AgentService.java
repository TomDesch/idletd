package io.github.stealingdapenta.idletd.agent;

import static io.github.stealingdapenta.idletd.Idletd.LOGGER;

import io.github.stealingdapenta.idletd.agent.mainagent.MainAgent;
import io.github.stealingdapenta.idletd.agent.mainagent.MainAgentHealthBar;
import io.github.stealingdapenta.idletd.agent.npc.AgentNPC;
import io.github.stealingdapenta.idletd.idlelocation.IdleLocationService;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.skin.SkinService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class AgentService {
    private final AgentRepository agentRepository;
    private final IdlePlayerService idlePlayerService;
    private final IdleLocationService idleLocationService;
    private final SkinService skinService;
    private final AgentStatsService agentStatsService;

    public void despawnAndDestroyNPC(Agent agent) {
        NPC npc = agent.getAgentNPC().getNpc();
        if (Objects.nonNull(npc)) {
            agent.getAgentNPC().getNpc().despawn();
            CitizensAPI.getNPCRegistry().deregister(npc);
        }
        agent.getAgentNPC().setNpc(null);
    }

    public void summonNPC(Agent agent) {

        MainAgent mainAgent = MainAgent.builder().agentNPC(agent.getAgentNPC()).agentStats(agent.getAgentStats())
                                       .build(); // todo fix so its actually stored and retrieved as mainAgent
        mainAgent.setMainAgentHealthBar(new MainAgentHealthBar());

        AgentNPC agentNPC = agent.getAgentNPC();
        Player owner = idlePlayerService.getPlayer(agent.getPlayerUUID());

        if (Objects.nonNull(agentNPC)) {
            if (Objects.nonNull(agentNPC.getNpc())) {
                LOGGER.info("Attempted to summon an NPC for an agent that already has one.");
                return;
            }
        } else {
            agentNPC = AgentNPC.builder()
                               .location(fetchLocationIfNull(agent))
                               .currentSkin(agent.getFetchedSkin())
                               .target(owner).name(agent.getName())
                               .build();
            agent.setAgentNPC(agentNPC);
        }

        agentNPC.setCurrentSkin(agent.getFetchedSkin());
        agentNPC.setLocation(agent.getFetchedLocation());
        agentNPC.spawn();
        agentNPC.updateTarget();
    }

    private void targetterTask(Agent agent) {
        // todo (one task for all agents pls) <= in Manager!
    }

    public List<Agent> findAllForPlayer(IdlePlayer idlePlayer) {
        List<Agent> agents = findAllForPlayer(idlePlayer.getPlayerUUID());
        if (Objects.isNull(agents) || agents.isEmpty()) {
            LOGGER.info("No agents found in DB for " + idlePlayer.getPlayerUUID()
                                                                 .toString());
            return agents;
        }

        LOGGER.info("Amount of agents found: " + agents.size());
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

    public long saveAgent(Agent agent) {
        return agentRepository.saveAgent(agent);
    }

    public void updateAgent(Agent agent) {
        agentRepository.updateAgent(agent);
    }

    private void fetchFields(Agent agent) {
        fetchIdlePlayerIfNull(agent);
        fetchLocationIfNull(agent);
        fetchSkinIfNull(agent);
        fetchStatsIfNull(agent);
    }

    private void fetchStatsIfNull(Agent agent) {
        if (Objects.isNull(agent.getAgentStats())) {
            agent.setAgentStats(agentStatsService.getAgentStats(agent));
        }
    }

    private void fetchSkinIfNull(Agent agent) {
        if (Objects.isNull(agent.getFetchedSkin())) {
            agent.setFetchedSkin(skinService.getSkin(agent.getActiveSkinId()));
        }
    }

    private void fetchIdlePlayerIfNull(Agent agent) {
        if (Objects.isNull(agent.getFetchedPlayer())) {
            agent.setFetchedPlayer(idlePlayerService.getIdlePlayer(agent.getPlayerUUID()));
        }
    }

    private Location fetchLocationIfNull(Agent agent) {
        if (Objects.isNull(agent.getFetchedLocation())) {
            agent.setFetchedLocation(idleLocationService.getLocation(agent.getFkLocation()));
        }
        return agent.getFetchedLocation();
    }
}
