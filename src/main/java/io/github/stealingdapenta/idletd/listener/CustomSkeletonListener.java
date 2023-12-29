package io.github.stealingdapenta.idletd.listener;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.agent.npc.AgentNPCHandler;
import io.github.stealingdapenta.idletd.custommob.CustomMobHandler;
import io.github.stealingdapenta.idletd.custommob.MobWrapper;
import io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

@RequiredArgsConstructor
public class CustomSkeletonListener implements Listener {

    private final CustomMobHandler customMobHandler;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSkeletonShoot(EntityShootBowEvent event) {
        LivingEntity shooter = event.getEntity();
        Arrow originalArrow = (Arrow) event.getProjectile();

        if (!customMobHandler.isCustomMob(shooter)) {
            return;
        }

        event.setCancelled(true);

        Entity targetEntity = ((Skeleton) shooter).getTarget();
        if (Objects.isNull(targetEntity)) {
            return;
        }

        Location targetLocation = targetEntity.getLocation();
        targetLocation = targetLocation.add(0, 1.5, 0); // adjusting to height

        if (targetIsOutOfRange(CustomMob.createFrom(shooter), targetLocation)) {
            shooter.getWorld()
                   .spawnParticle(Particle.VILLAGER_ANGRY, shooter.getEyeLocation()
                                                                  .add(0, 1, 0), 2);
            return;
        }

        Location arrowLocation = originalArrow.getLocation();

        int skeletonLevel = customMobHandler.getMobLevel(shooter);
        double flySpeed = 1 + ((double) skeletonLevel / 100);

        createParabolicParticleTrail(arrowLocation, 1.5, targetLocation, flySpeed);
    }

    private boolean targetIsOutOfRange(MobWrapper attacker, Location targetLocation) {
        return attacker.getSummonedEntity()
                       .getLocation()
                       .distanceSquared(targetLocation) > attacker.getAttackRangeSquared();
    }


    public void createParabolicParticleTrail(Location startLocation, double offset, Location targetLocation, double flySpeed) {
        double startX = startLocation.getX();
        double startY = startLocation.getY();
        double startZ = startLocation.getZ();

        double targetX = targetLocation.getX() + getRandomOffset(offset);
        double targetY = targetLocation.getY() + getRandomOffset(offset);
        double targetZ = targetLocation.getZ() + getRandomOffset(offset);

        double distance = startLocation.distance(targetLocation);
        double arcHeight = distance * 0.06; // Adjust as needed for the desired parabolic effect

        int numberOfParticles = (int) (distance / 0.25);
        double step = 1.0 / numberOfParticles;

        double index = 0.0;

        while (index <= 2.0) {
            double x = startX + index * (targetX - startX);
            double y = startY + index * (targetY - startY) + arcHeight * Math.sin(Math.PI * index);
            double z = startZ + index * (targetZ - startZ);
            Location particleLocation = new Location(startLocation.getWorld(), x, y, z);

            if (particleCollidesWithEntity(particleLocation)) {
                List<LivingEntity> collidedEntities = getCollidedEntities(particleLocation);
//                if (!collidedEntities.stream()
//                                     .allMatch(customMobHandler::isCustomMobOrCustomArmorStand)) {
//                    animateEntityImpactParticles(particleLocation);
//                    return;
//                }
                LivingEntity possibleNPC = collidedEntities.stream()
                                                           .filter(AgentNPCHandler::isNPC)
                                                           .findFirst()
                                                           .orElse(null);
                if (Objects.nonNull(possibleNPC)) {
                    System.out.printf("'Arrow' hit an NPC !! %s%n", possibleNPC.getName());
                    animateEntityImpactParticles(particleLocation);
                    return;
                }
            } else if (particleCollidesWithSolidBlock(particleLocation)) {
                animateGroundImpactParticles(particleLocation);
                return;
            }

            Color color = Color.fromRGB(123, 13, 123);

            spawnColoredParticleDelayed(particleLocation, color, (int) (index * numberOfParticles * flySpeed)); // Introduce a delay

            index += step;
        }
    }

    private double getRandomOffset(double maxOffset) {
        return (Math.random() * 2 - 1) * maxOffset;
    }

    private void spawnColoredParticleDelayed(Location location, Color color, int delayTicks) {
        Bukkit.getScheduler()
              .runTaskLater(Idletd.getInstance(), () -> spawnColoredParticle(location, color), delayTicks);
    }

    private void spawnColoredParticle(Location location, Color color) {
        location.getWorld()
                .spawnParticle(Particle.REDSTONE, location, 0, 0, 0, 0, new Particle.DustOptions(color, 1));
    }

    private boolean particleCollidesWithEntity(Location location) {
        return location.getWorld()
                       .getNearbyEntities(location, 0.15, 0.15, 0.15)
                       .stream()
                       .anyMatch(LivingEntity.class::isInstance);
    }

    private boolean particleCollidesWithSolidBlock(Location location) {
        return !location.getBlock()
                        .isPassable();
    }

    private List<LivingEntity> getCollidedEntities(Location location) {
        return location.getWorld()
                       .getNearbyEntities(location, 0.15, 0.15, 0.15)
                       .stream()
                       .filter(LivingEntity.class::isInstance)
                       .map(LivingEntity.class::cast)
                       .toList();
    }

    private void animateEntityImpactParticles(Location location) {
        location.getWorld()
                .spawnParticle(Particle.EXPLOSION_NORMAL, location, 3, 0, 0, 0, 0);
    }

    private void animateGroundImpactParticles(Location location) {
        location.getWorld()
                .spawnParticle(Particle.SMOKE_NORMAL, location, 15, 0, 0, 0, 0.1);
    }

}
