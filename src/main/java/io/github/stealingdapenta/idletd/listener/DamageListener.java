package io.github.stealingdapenta.idletd.listener;

import io.github.stealingdapenta.idletd.custommob.CustomMobHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Particle;
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
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class DamageListener implements Listener {

    // todo recalculate dmg player to custom mob
    // todo recalculate dmg custom mob to player or Agent NPC
    // todo calculate dmg done by Agent to custom mob
    // todo cancel dmg done by agent to player or agent

    private final CustomMobHandler customMobHandler;


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

            if ((isMob(target) && customMobHandler.isCustomMob((Mob) target))) {
                if (projectile.getShooter() instanceof LivingEntity shooter && customMobHandler.isCustomMob(shooter)) {
                    cancelDamageEvent(event);
                }

            }
        }
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

    private void cancelDamageEvent(EntityDamageEvent event) {
        event.setDamage(0);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSkeletonShoot(EntityShootBowEvent event) {
        LivingEntity shooter = event.getEntity();
        Arrow originalArrow = (Arrow) event.getProjectile();

        if (!customMobHandler.isCustomMob(shooter)) return;

        event.setCancelled(true);

        Entity targetEntity = ((Skeleton) shooter).getTarget();
        if (Objects.isNull(targetEntity)) return;

        Location targetLocation = targetEntity.getLocation();
        targetLocation = targetLocation.add(0, 1.5, 0); // adjusting to height
        Location arrowLocation = originalArrow.getLocation();

        Vector targetDirection = targetLocation.toVector().subtract(arrowLocation.toVector()).normalize();

        double accuracy = 0.2; // Adjust this value for accuracy, 0 being 100% accurate
        Vector randomizedDirection = addRandomnessToArrowVector(targetDirection, accuracy);
        spawnParticleTrail(arrowLocation, randomizedDirection, 0.1);
    }

    private Vector addRandomnessToArrowVector(Vector vector, double accuracy) {
        double x = vector.getX() + (Math.random() * 2 - 1) * accuracy;
        double z = vector.getZ() + (Math.random() * 2 - 1) * accuracy;

        return new Vector(x, vector.getY(), z).normalize();
    }

    private void spawnParticleTrail(Location location, Vector direction, double offset) {
        boolean collisionOccurred = false;
        int i = 0;
        double gravity = -0.01; // Negative gravity for descending movement
        double initialUpwardSpeed = 0.1; // Adjust this value for initial upward movement

        while (!collisionOccurred) {
            double x = offset * i * direction.getX();
            double y = initialUpwardSpeed * i + 0.5 * gravity * i * i; // Quadratic function for parabolic shape
            double z = offset * i * direction.getZ();

            Location particleLocation = location.clone().add(x, y, z);

            if (particleCollidesWithEntity(particleLocation)) {
                List<LivingEntity> collidedEntities = getCollidedEntities(particleLocation);
                if (collidedEntities.stream().allMatch(customMobHandler::isCustomMob)) {
                    location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, particleLocation, 1);
                } else {
                    animateEntityImpactParticles(particleLocation);
                    collisionOccurred = true;
                }
            } else if (particleCollidesWithSolidBlock(particleLocation)) {
                animateGroundImpactParticles(particleLocation);
                collisionOccurred = true;
            } else {
                location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, particleLocation, 1);
            }

            i++;

            if (i > 500) { // Failsafe break
                collisionOccurred = true;
            }
        }
    }


    private boolean particleCollidesWithEntity(Location location) {
        return location.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5).stream().anyMatch(LivingEntity.class::isInstance);
    }

    private boolean particleCollidesWithSolidBlock(Location location) {
        return !location.getBlock().isPassable();
    }

    private List<LivingEntity> getCollidedEntities(Location location) {
        return location.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5)
                       .stream()
                       .filter(LivingEntity.class::isInstance)
                       .map(LivingEntity.class::cast)
                       .toList();
    }

    private void animateEntityImpactParticles(Location location) {
        location.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, location, 3, 0, 0, 0, 0);
    }

    private void animateGroundImpactParticles(Location location) {
        location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 15, 0, 0, 0, 0.1);
    }
}
