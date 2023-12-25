package io.github.stealingdapenta.idletd.agent.npc;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;

@RequiredArgsConstructor
public class AgentNPCHandler {

    public static boolean isNPC(Entity entity) {
        return entity.hasMetadata("NPC");
    }



}
