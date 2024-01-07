package io.github.stealingdapenta.idletd.custommob;

import static io.github.stealingdapenta.idletd.custommob.CustomMobHandler.getMobLevel;
import static io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob.createFrom;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

@RequiredArgsConstructor
public class MobAttackHandler {

    private static final int SECOND_IN_MS = 1000;

    // map<Living custom mob, last attack time
    private final HashMap<CustomMob, Long> livingCustomMobs = new HashMap<>();

    public void addCustomMob(CustomMob customMob) {
        livingCustomMobs.put(customMob, System.currentTimeMillis());
    }

    public void removeDeadMobs() {
        livingCustomMobs.keySet()
                        .removeIf(this::mobRemoved);
    }

    private boolean mobRemoved(CustomMob customMob) {
        return Objects.isNull(customMob) || Objects.isNull(customMob.getMob()) || !customMob.getMob()
                                                                                            .isValid() || customMob.getMob()
                                                                                                                   .isDead();
    }

    private void checkAllAttacks() {
        // todo have one task that loops over them, removes dead mobs, checks if remaining can attack

        removeDeadMobs();

        livingCustomMobs.keySet()
                        .forEach(customMob -> {
                            if (canAttack(customMob, livingCustomMobs.get(customMob))) {
                                doAttack(customMob);
                            }
                        });
    }


    private boolean canAttack(CustomMob customMob, long lastAttack) {
        long msSinceLastAttack = System.currentTimeMillis() - lastAttack;
        MobWrapper mobWrapper = createFrom(customMob.getMob());
        double attackSpeedPerSecond = mobWrapper.getAttackSpeed();

        return enoughTimePassed(attackSpeedPerSecond, msSinceLastAttack);
    }

    private boolean enoughTimePassed(double minimalDelay, long msPassed) {
        return minimalDelay * SECOND_IN_MS <= msPassed;
    }

    private void doAttack(CustomMob customMob) {
        switch (customMob.getAttackType()) {
            case MELEE -> doMeleeAttack(customMob);
            case RANGED -> doRangedAttack(customMob);
        }

        this.livingCustomMobs.put(customMob, System.currentTimeMillis());
    }


    private void doMeleeAttack(CustomMob customMob) {
        // todo do atk animation with protocollib

    }

    private void doRangedAttack(CustomMob customMob) {
        Mob shooter = customMob.getMob();

        Entity targetEntity = shooter.getTarget();
        if (Objects.isNull(targetEntity)) {
            return;
        }

        Location targetLocation = targetEntity.getLocation();
        targetLocation = targetLocation.add(0, 1.5, 0); // adjusting to height

        MobWrapper shooterWrapper = createFrom(shooter);

        if (targetIsOutOfRange(shooterWrapper, targetLocation)) {
            shooter.getWorld()
                   .spawnParticle(Particle.VILLAGER_ANGRY, shooter.getEyeLocation(), 2);
            return;
        }

        Location arrowLaunchLocation = shooter.getEyeLocation()
                                              .add(0, -0.2, 0);

        int shooterLevel = getMobLevel(shooter);
        // todo move this to field?
        double flySpeed = 1 + ((double) shooterLevel / 100);

        // todo DO BOW ANIMATION HERE

        createParabolicParticleTrail(arrowLaunchLocation, 1.5, targetLocation, flySpeed);

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
                LivingEntity possibleNPC = collidedEntities.stream()
                                                           .filter(CitizensAPI.getNPCRegistry()::isNPC)
                                                           .findFirst()
                                                           .orElse(null);
                if (Objects.nonNull(possibleNPC)) {
                    System.out.printf("'Arrow' hit an NPC !! %s%n", possibleNPC.getName());
                    // todo deal dmg to npc
                    animateEntityImpactParticles(particleLocation);
                    return;
                }
            } else if (particleCollidesWithSolidBlock(particleLocation)) {
                animateGroundImpactParticles(particleLocation);
                return;
            }

            Color color = Color.fromRGB(123, 13, 123);

            spawnColoredParticleDelayed(particleLocation, color, (int) (index * numberOfParticles / flySpeed)); // Introduce a delay

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
