package io.github.stealingdapenta.idletd.listener;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.custommob.CustomMobHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Objects;

@RequiredArgsConstructor
public class DamageListener implements Listener {

    // todo recalculate dmg player to custom mob
    // todo recalculate dmg custom mob to player or Agent NPC
    // todo calculate dmg done by Agent to custom mob
    // todo cancel dmg done by agent to player or agent

    private final CustomMobHandler customMobHandler;

    private Vector addRandomnessToArrowVector(Vector vector, double accuracy) {
        double x = vector.getX() + (Math.random() * 2 - 1) * accuracy;
        double z = vector.getZ() + (Math.random() * 2 - 1) * accuracy;

        return new Vector(x, vector.getY(), z).normalize();
    }


    private boolean isMob(Entity entity) {
        return entity instanceof Mob;
    }

    private boolean isProjectile(Entity entity) {
        return entity instanceof Projectile;
    }

    private boolean isCustomMobDamage(Entity source, Entity target) {
        return isMob(source) && customMobHandler.isCustomMob((Mob) source) && isMob(target) && customMobHandler.isCustomMob((Mob) target);
    }

    private boolean isCustomArrow(Projectile projectile) {
        return projectile.getPersistentDataContainer().has(getCustomArrowNSK(), PersistentDataType.BYTE);
    }

    private void cancelDamageEvent(EntityDamageEvent event) {
        event.setDamage(0);
        event.setCancelled(true);
    }

    private void cancelCustomMobArrowDamage(EntityDamageByEntityEvent event, Projectile projectile) {
        cancelDamageEvent(event);
        // Set the arrow's velocity to keep it flying towards its initial destination
        Vector originalVelocity = projectile.getVelocity();
        projectile.setVelocity(originalVelocity);
    }

    private NamespacedKey getCustomArrowNSK() {
        return new NamespacedKey(Idletd.getInstance(), "customArrow");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSkeletonShoot(EntityShootBowEvent event) {
        LivingEntity shooter = event.getEntity();
        Arrow originalArrow = (Arrow) event.getProjectile();

        if (!customMobHandler.isCustomMob(shooter)) return;

        event.setCancelled(true);

        Arrow newArrow = shooter.launchProjectile(Arrow.class);

        Entity targetEntity = ((Skeleton) shooter).getTarget();
        if (Objects.isNull(targetEntity)) return;

        Location targetLocation = targetEntity.getLocation();
        Location arrowLocation = originalArrow.getLocation();

        Vector targetDirection = targetLocation.toVector().subtract(arrowLocation.toVector()).normalize();

        double accuracy = 0.2; // Adjust this value for accuracy, 0 being 100% accurate
        Vector randomizedDirection = addRandomnessToArrowVector(targetDirection, accuracy);
        newArrow.setVelocity(randomizedDirection.multiply(newArrow.getVelocity().length()));

        // Add a persistent tag to the arrow to identify it as a custom arrow
        newArrow.getPersistentDataContainer().set(getCustomArrowNSK(), PersistentDataType.BYTE, (byte) 1);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelUnwantedDamageEvents(EntityDamageByEntityEvent event) {
        Entity source = event.getDamager();
        Entity target = event.getEntity();

        if (isCustomMobDamage(source, target)) {
            cancelDamageEvent(event);
            return;
        }

        if (isProjectile(source)) {
            Projectile projectile = (Projectile) source;
            if (isCustomArrow(projectile) && (isMob(target) && customMobHandler.isCustomMob((Mob) target))) {
                cancelCustomMobArrowDamage(event, projectile);
            }
        }
    }
}
