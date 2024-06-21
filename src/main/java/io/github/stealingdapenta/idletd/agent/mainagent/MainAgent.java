package io.github.stealingdapenta.idletd.agent.mainagent;

import io.github.stealingdapenta.idletd.agent.Agent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.eclipse.sisu.PostConstruct;


@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder(toBuilder = true)
public class MainAgent extends Agent {

    private MainAgentHealthBar mainAgentHealthBar;

    @PostConstruct
    public void postConstruct() {
        this.mainAgentHealthBar = new MainAgentHealthBar();
        this.mainAgentHealthBar.createHealthBar(this);
    }

}
