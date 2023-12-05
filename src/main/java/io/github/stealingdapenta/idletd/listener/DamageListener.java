package io.github.stealingdapenta.idletd.listener;

import io.github.stealingdapenta.idletd.custommob.CustomMobHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static io.github.stealingdapenta.idletd.Idletd.logger;

@RequiredArgsConstructor
public class DamageListener implements Listener {

    // todo recalculate dmg player to custom mob
    // todo recalculate dmg custom mob to player or Agent NPC
    // todo calculate dmg done by Agent to custom mob
    // todo cancel dmg done by agent to player or agent

    private final CustomMobHandler customMobHandler;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void damageEvent(EntityDamageByEntityEvent event) {
        Entity source = event.getDamager();
        Entity target = event.getEntity();

        if (isMob(source) && customMobHandler.isCustomMob((Mob) source)
                && isMob(target) && customMobHandler.isCustomMob((Mob) target)) {
            logger.info("Cancelled damage from custom mob to custom mob.");
            event.setDamage(0);
            return;
        }

        if (isArrow(source)) {
            Arrow arrow = (Arrow) source;
            if (arrow.getShooter() instanceof Mob shooter) {
                if (customMobHandler.isCustomMob(shooter)) {
                    logger.info("Cancelled damage from custom mob arrow to custom mob.");
                    event.setDamage(0);
                }
            }
        }
    }

    private boolean isMob(Entity entity) {
        return entity instanceof Mob;
    }

    private boolean isArrow(Entity entity) {
        return entity instanceof Arrow;
    }


}
