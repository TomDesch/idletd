package io.github.stealingdapenta.idletd.listener;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.agent.AgentManager;
import io.github.stealingdapenta.idletd.agent.npc.AgentNPCHandler;
import io.github.stealingdapenta.idletd.custommob.CustomMobHandler;
import io.github.stealingdapenta.idletd.custommob.MobWrapper;
import io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerManager;
import io.github.stealingdapenta.idletd.idleplayer.battlestats.BattleStats;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class DamageListener implements Listener {

    // todo recalculate dmg player to custom mob
    // todo recalculate dmg custom mob to player or Agent NPC
    // todo calculate dmg done by Agent to custom mob
    // todo cancel dmg done by agent to player or agent

    private final CustomMobHandler customMobHandler;
    private final AgentManager agentManager;
    private final IdlePlayerManager idlePlayerManager;


    @EventHandler(priority = EventPriority.MONITOR)
    public void calculateDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;

        Entity source = event.getDamager();
        Entity target = event.getEntity();

        if (customMobHandler.isCustomMob(source)) {
            System.out.println("Custom mob is doing a hit :)");
        }

        if (AgentNPCHandler.isNPC(target)) {
            System.out.println("Agent NPC is taking a hit :)");
        }

        if (customMobHandler.isCustomMob(source) && AgentNPCHandler.isNPC(target)) {
            handleMobHittingAgent((LivingEntity) source, (NPC) target, event);

        } else if (AgentNPCHandler.isNPC(source) && customMobHandler.isCustomMob(target)) {
            handleAgentHittingMob((NPC) source, (LivingEntity) target, event);

        } else if (source instanceof Player sourcePlayer && customMobHandler.isCustomMob(target)) {
            handlePlayerHittingMob(sourcePlayer, (LivingEntity) target, event);
        }
    }

    private void handleMobHittingAgent(LivingEntity mob, NPC agent, EntityDamageByEntityEvent event) {
        // Implement logic for calculating and applying damage todo
        System.out.printf("Mob %s is hitting agent %s for %s damage.%n", mob.getName(), agent.getName(), event.getFinalDamage());
    }

    private void handleAgentHittingMob(NPC agent, LivingEntity mob, EntityDamageByEntityEvent event) {
        // Implement logic for calculating and applying damage todo
    }

    private void handlePlayerHittingMob(Player player, LivingEntity mob, EntityDamageByEntityEvent event) {
        IdlePlayer attackingPlayer = idlePlayerManager.getOnlineIdlePlayer(player);
        BattleStats attackingStats = attackingPlayer.getFetchedBattleStats();
        MobWrapper defendingMobStats = CustomMob.createFrom(mob);
        // todo fix it all like this this is all botched and not functional
        double damage = attackingStats.getAttackPower();

        if (isMelee(event.getCause())) {
            if (isPlayerHoldingAxe(player)) {
                damage = damage - (damage * defendingMobStats.getAxe_resistance());
            } else {
                damage = damage - (damage * defendingMobStats.getSword_resistance());
            }
        }

        event.setDamage(damage);
        // Implement logic for calculating and applying damage todo

    }

    private boolean isPlayerHoldingAxe(Player player) {
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();

        return mainHandItem.getType() == Material.DIAMOND_AXE || mainHandItem.getType() == Material.GOLDEN_AXE
                || mainHandItem.getType() == Material.IRON_AXE || mainHandItem.getType() == Material.STONE_AXE
                || mainHandItem.getType() == Material.WOODEN_AXE;
    }

    private boolean isMelee(DamageCause attack) {
        return attack.equals(DamageCause.ENTITY_ATTACK) ||
                attack.equals(DamageCause.ENTITY_SWEEP_ATTACK);
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

        int skeletonLevel = customMobHandler.getMobLevel(shooter);
        double flySpeed = 1 + ((double) skeletonLevel / 100);

        createParabolicParticleTrail(arrowLocation, 1.5, targetLocation, flySpeed);
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
                if (!collidedEntities.stream().allMatch(customMobHandler::isCustomMobOrCustomArmorStand)) {
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
        Bukkit.getScheduler().runTaskLater(Idletd.getInstance(), () -> spawnColoredParticle(location, color), delayTicks);
    }

    private void spawnColoredParticle(Location location, Color color) {
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 0, 0, 0, 0, new Particle.DustOptions(color, 1));
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
