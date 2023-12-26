package io.github.stealingdapenta.idletd.agent;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AgentStats {

    private int agentId;

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
