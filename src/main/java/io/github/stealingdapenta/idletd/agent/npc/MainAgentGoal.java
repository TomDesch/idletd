package io.github.stealingdapenta.idletd.agent.npc;

import io.github.stealingdapenta.idletd.plot.Plot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.GoalSelector;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Getter
@Setter
public class MainAgentGoal implements Goal {

    private final Player player;
    private final Plot plot;
    private final Location centerLocation;
    private NPC npc;

    @Override
    public void reset() {
    }

    @Override
    public void run(GoalSelector goalSelector) {
        // the Main Agent is statically placed, and won't ever need to run
    }

    @Override
    public boolean shouldExecute(GoalSelector goalSelector) {
        return true;
    }
}
