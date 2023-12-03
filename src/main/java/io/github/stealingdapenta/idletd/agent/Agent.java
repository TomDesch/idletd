package io.github.stealingdapenta.idletd.agent;

import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.Objects;
import java.util.UUID;

@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Agent {
    // table fields
    private int id;
    private UUID playerUUID;
    private AgentType agentType;
    private int fkLocation;

    // todo update:
    // Add active skin ID


    // calculated fields
    private IdlePlayer fetchedPlayer;
    private Location fetchedLocation;

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
