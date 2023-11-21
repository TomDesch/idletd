package io.github.stealingdapenta.idletd.listener;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.service.custommob.CustomMobHandler;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Objects;

@RequiredArgsConstructor
public class CustomMobListener implements Listener {
    private final HashMap<LivingEntity, BukkitTask> entitiesWithActiveHealthBars = new HashMap<>();
    private final CustomMobHandler customMobHandler;


    private static Component createHealthBar(double currentHealth, double maxHealth) {
        double percentAlive = currentHealth / maxHealth;
        int aliveBarLength = (int) (percentAlive * 16);
        int deadBarLength = 10 - aliveBarLength;

        return Component.text(" ".repeat(Math.max(0, aliveBarLength)), TextColor.color(0, 255, 0), TextDecoration.STRIKETHROUGH, TextDecoration.BOLD)
                        .append(Component.text(" ".repeat(Math.max(0, deadBarLength)), TextColor.color(100, 100, 100), TextDecoration.STRIKETHROUGH, TextDecoration.BOLD));
    }

    @EventHandler
    public void displayHealthBar(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();
        BukkitTask existingHealthBar = null;


        if (!(damagedEntity instanceof LivingEntity livingDamagedEntity)) {
            return;
        }

        BukkitTask displayBarTask = new BukkitRunnable() {
            private int ticksLeft = 5 * 20; // Display for 5 seconds (20 ticks per second)

            @Override
            public void run() {
                if (ticksLeft <= 0) {
                    this.cancel();
                    livingDamagedEntity.setCustomNameVisible(false);
                    return;
                }

                double health = livingDamagedEntity.getHealth();
                double maxHealth = Objects.requireNonNull(livingDamagedEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();

                livingDamagedEntity.customName(createHealthBar(health, maxHealth));
                livingDamagedEntity.setCustomNameVisible(true);

                ticksLeft--;
            }
        }.runTaskTimer(Idletd.getInstance(), 0L, 1L); // Run every tick (1L)

        if (entitiesWithActiveHealthBars.containsKey(livingDamagedEntity)) {
            existingHealthBar = entitiesWithActiveHealthBars.get(livingDamagedEntity);
        }

        entitiesWithActiveHealthBars.put(livingDamagedEntity, displayBarTask);

        if (Objects.nonNull(existingHealthBar)) {
            existingHealthBar.cancel();
        }
    }

    @EventHandler
    public void checkDeadCustomMobs(EntityDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();

        if (customMobHandler.isCustomMob(livingEntity)) {
            customMobHandler.removeDeadMobsFromList();
        }
    }
}



