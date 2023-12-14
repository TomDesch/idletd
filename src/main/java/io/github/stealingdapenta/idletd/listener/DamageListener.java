package io.github.stealingdapenta.idletd.listener;

import io.github.stealingdapenta.idletd.custommob.CustomMobHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

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

        createParabolicParticleTrail(arrowLocation, 0.1, targetLocation);
    }


    public void createParabolicParticleTrail(Location startLocation, double offset, Location targetLocation) {
        double startX = startLocation.getX();
        double startY = startLocation.getY();
        double startZ = startLocation.getZ();

        double targetX = targetLocation.getX() + getRandomOffset(offset);
        double targetY = targetLocation.getY() + getRandomOffset(offset);
        double targetZ = targetLocation.getZ() + getRandomOffset(offset);

        double distance = startLocation.distance(targetLocation);
        double arcHeight = distance * 0.2; // Adjust as needed for the desired parabolic effect

        int numberOfParticles = (int) (distance / 0.25);
        double step = 1.0 / numberOfParticles;

        for (int i = 0; i <= numberOfParticles; i++) {
            double index = i * step;
            double x = startX + index * (targetX - startX);
            double y = startY + index * (targetY - startY) + arcHeight * Math.sin(Math.PI * index);
            double z = startZ + index * (targetZ - startZ);
            Location particleLocation = new Location(startLocation.getWorld(), x, y, z);

            if (particleCollidesWithEntity(particleLocation)) {
                List<LivingEntity> collidedEntities = getCollidedEntities(particleLocation);
                if (!collidedEntities.stream().allMatch(customMobHandler::isCustomMobOrCustomArmorStand)) {
                    animateEntityImpactParticles(particleLocation);
                    return;
                }
            } else if (particleCollidesWithSolidBlock(particleLocation)) {
                animateGroundImpactParticles(particleLocation);
                return;
            }

            startLocation.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, particleLocation, 1);
        }
    }

    private double getRandomOffset(double maxOffset) {
        return (Math.random() * 2 - 1) * maxOffset;
    }

    private double calculateInitialUpwardSpeed(Location source, Location target, double gravity) {
        double distance = target.distance(source);
        return Math.sqrt(2 * gravity * distance);
    }

    private boolean particleCollidesWithEntity(Location location) {
        return location.getWorld().getNearbyEntities(location, 0.15, 0.15, 0.15).stream().anyMatch(LivingEntity.class::isInstance);
    }

    private boolean particleCollidesWithSolidBlock(Location location) {
        return !location.getBlock().isPassable();
    }

    private List<LivingEntity> getCollidedEntities(Location location) {
        return location.getWorld().getNearbyEntities(location, 0.15, 0.15, 0.15)
                       .stream()
                       .filter(LivingEntity.class::isInstance)
                       .map(LivingEntity.class::cast)
                       .toList();
    }

    private void animateEntityImpactParticles(Location location) {
        location.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, location, 3, 0, 0, 0, 0);
    }

    private void animateGroundImpactParticles(Location location) {
        location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 15, 0, 0, 0, 0.1);
    }
}
