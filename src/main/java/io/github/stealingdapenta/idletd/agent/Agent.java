package io.github.stealingdapenta.idletd.agent;

import io.github.stealingdapenta.idletd.agent.npc.AgentNPC;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.skin.Skin;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bukkit.Location;

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Agent {
    // table fields
    private int id;
    private UUID playerUUID;
    private AgentType agentType;
    private int fkLocation;
    private int activeSkinId;


    // calculated fields
    private AgentStats fetchedAgentStats;
    private IdlePlayer fetchedPlayer;
    private Location fetchedLocation;
    private Skin fetchedSkin;
    private AgentNPC agentNPC;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return id == agent.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
