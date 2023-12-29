package io.github.stealingdapenta.idletd.listener;

import io.github.stealingdapenta.idletd.custommob.CustomMobHandler;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.towerdefense.TowerDefense;
import io.github.stealingdapenta.idletd.towerdefense.TowerDefenseManager;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;

@RequiredArgsConstructor
public class CustomMobListener implements Listener {
    private final CustomMobHandler customMobHandler;
    private final IdlePlayerService idlePlayerService;
    private final TowerDefenseManager towerDefenseManager;


    @EventHandler
    public void checkDeadCustomMobs(EntityDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();

        if (customMobHandler.isCustomMob(livingEntity)) {
            customMobHandler.removeDeadMobsFromList();
        }
    }

    @EventHandler
    public void checkWrongTarget(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Mob mob)) {
            return;
        }

        Entity target = event.getTarget();

        if (Objects.isNull(target) || !customMobHandler.isCustomMob(mob) || CitizensAPI.getNPCRegistry()
                                                                                       .isNPC(target)) {
            return; // Assuming the NPC is the correct one.
        }

        String uuid = customMobHandler.getLinkedPlayerUUID(mob);
        IdlePlayer playerLinkedToMob = idlePlayerService.getIdlePlayer(uuid);

        if (Objects.isNull(uuid) || Objects.isNull(playerLinkedToMob)) {
            return;
        }

        TowerDefense towerDefense = towerDefenseManager.getActiveTDGame(playerLinkedToMob);

        if (Objects.isNull(towerDefense)) {
            return;
        }

        customMobHandler.setTarget(mob, towerDefense.getTargetAgent());
    }
}



