package io.github.stealingdapenta.idletd.agent;

import io.github.stealingdapenta.idletd.idlelocation.IdleLocationService;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.Objects;

@RequiredArgsConstructor
public class AgentService {
    private final AgentRepository agentRepository;
    private final IdlePlayerService idlePlayerService;
    private final IdleLocationService idleLocationService;

    public Agent getAgent(int id) {
        Agent agent = agentRepository.getAgent(id);
        fetchIdlePlayerIfNull(agent);
        fetchLocationIfNull(agent);
        return agent;
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

    public IdlePlayer fetchIdlePlayerIfNull(Agent agent) {
        if (Objects.isNull(agent.getFetchedPlayer())) {
            agent.setFetchedPlayer(idlePlayerService.getIdlePlayer(agent.getPlayerUUID()));
        }
        return agent.getFetchedPlayer();
    }

    public Location fetchLocationIfNull(Agent agent) {
        if (Objects.isNull(agent.getFetchedLocation())) {
            agent.setFetchedLocation(idleLocationService.getLocation(agent.getFkLocation()));
        }
        return agent.getFetchedLocation();
    }
}
