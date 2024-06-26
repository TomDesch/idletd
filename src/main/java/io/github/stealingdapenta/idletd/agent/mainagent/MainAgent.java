package io.github.stealingdapenta.idletd.agent.mainagent;

import static io.github.stealingdapenta.idletd.Idletd.LOGGER;

import io.github.stealingdapenta.idletd.agent.Agent;
import io.github.stealingdapenta.idletd.agent.AgentStats;
import io.github.stealingdapenta.idletd.agent.npc.AgentNPC;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.skin.Skin;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bukkit.Location;
import org.eclipse.sisu.PostConstruct;


@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class MainAgent extends Agent {

    private MainAgentHealthBar mainAgentHealthBar;

    private static final String NAME = "Main Agent";

    public MainAgent(int id, UUID playerUUID, int fkLocation, int activeSkinId, AgentStats agentStats, IdlePlayer fetchedPlayer, Location fetchedLocation,
                     Skin fetchedSkin, AgentNPC agentNPC, MainAgentHealthBar mainAgentHealthBar) {
        super(id, playerUUID, fkLocation, activeSkinId, NAME, agentStats, fetchedPlayer, fetchedLocation, fetchedSkin, agentNPC);
        this.mainAgentHealthBar = mainAgentHealthBar;
    }

    public MainAgent(MainAgentHealthBar mainAgentHealthBar, String name) {
        this.mainAgentHealthBar = mainAgentHealthBar;
    }

    public MainAgent(AgentBuilder<?, ?> b, MainAgentHealthBar mainAgentHealthBar, String name) {
        super(b);
        this.mainAgentHealthBar = mainAgentHealthBar;
    }

    @PostConstruct
    public void postConstruct() {
        LOGGER.info("Entering post construct");
        this.mainAgentHealthBar = new MainAgentHealthBar();
        this.mainAgentHealthBar.createHealthBar(this);
    }

    public MainAgentStats getMainAgentStats() {
        return (MainAgentStats) this.getAgentStats();
    }

}