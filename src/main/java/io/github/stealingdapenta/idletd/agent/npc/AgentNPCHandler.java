package io.github.stealingdapenta.idletd.agent.npc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.entity.Entity;

@RequiredArgsConstructor
@Getter
public class AgentNPCHandler {

    public static final NPCRegistry registry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());

    public static boolean isNPC(Entity entity) {
        return entity.hasMetadata("NPC");
    }



}
