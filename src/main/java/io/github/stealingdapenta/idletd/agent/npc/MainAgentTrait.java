package io.github.stealingdapenta.idletd.agent.npc;

import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;

public class MainAgentTrait extends Trait {
    private Location location;

    protected MainAgentTrait(String name) {
        super(name);
    }

    @EventHandler
    public void click(NPCClickEvent event) {
        if (event.getNPC() != this.npc) return;

        // todo handle upgrade screen and/or settings screen here
    }

    // Called every tick
    @Override
    public void run() {
        // this.npc
    }
}
