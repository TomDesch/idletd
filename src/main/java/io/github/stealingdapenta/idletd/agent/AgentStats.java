package io.github.stealingdapenta.idletd.agent;

import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class AgentStats {

    private final int agentId;

    private double currentHealth;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentStats that)) {
            return false;
        }
        return agentId == that.agentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentId);
    }
}
