package io.github.stealingdapenta.idletd.service.agent;

import io.github.stealingdapenta.idletd.plot.Plot;
import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.GoalSelector;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MainAgentGoal implements Goal {

    private final Player player;
    private final Plot plot;
    private final Location centerLocation;
    private NPC npc;

    public MainAgentGoal(Player player, Plot plot) {
        this.player = player;
        this.plot = plot;

        this.centerLocation = calculatePosition();

    }

    @Override
    public void reset() {
    }

    @Override
    public void run(GoalSelector goalSelector) {
        // the Main Agent is staticly places, and won't ever need to run
    }

    @Override
    public boolean shouldExecute(GoalSelector goalSelector) {
        return true;
    }


    private Location calculatePosition() {
        Location spawnLocation = plot.getPlayerSpawnPoint();
        spawnLocation.add(0d, -10d, -8d);
        spawnLocation.setYaw(180); // Facing north
        spawnLocation.setPitch(0);
        return spawnLocation;
    }
}
