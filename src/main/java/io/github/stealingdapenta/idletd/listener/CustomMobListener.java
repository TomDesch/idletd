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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

@RequiredArgsConstructor
public class CustomMobListener implements Listener {

    private final CustomMobHandler customMobHandler = CustomMobHandler.getInstance();
    private final IdlePlayerService idlePlayerService;
    private final TowerDefenseManager towerDefenseManager;

    /**
     * @param event The method checks if the entity that is targeting is a wave mob. If it is, the event is cancelled. This prevents the mob from targeting the
     *              entity, thus stopping both the attack and the visual cues of the attack.
     */
    @EventHandler
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        if (!customMobHandler.isCustomMob(event.getEntity())) {
            return;
        }

        event.setCancelled(true);
    }

    /**
     * @param event EntityShootBowEvent from SpigotMC Cancels all the skeleton shoot events from custom mobs
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSkeletonShoot(EntityShootBowEvent event) {
        LivingEntity shooter = event.getEntity();

        if (!customMobHandler.isCustomMob(shooter)) {
            return;
        }

        event.setCancelled(true);
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



