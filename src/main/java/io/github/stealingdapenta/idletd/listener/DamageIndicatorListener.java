package io.github.stealingdapenta.idletd.listener;

import io.github.stealingdapenta.idletd.Idletd;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.concurrent.atomic.AtomicInteger;

import static net.royawesome.jlibnoise.MathHelper.round;

public class DamageIndicatorListener implements Listener {

    @EventHandler
    public void displayDamageIndicator(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();

        if (!(damagedEntity instanceof LivingEntity livingDamagedEntity)) {
            return;
        }

        Location initialLocation = this.calculateHitLocation(event.getDamager().getLocation(), livingDamagedEntity.getLocation());
        double damageDealt = event.getDamage();
        this.animateArmorStand(initialLocation, damageDealt);
    }

    private ArmorStand createArmorStand(Location initialLocation, double damageDealt) {
        ArmorStand armorStand = initialLocation.getWorld().spawn(initialLocation, ArmorStand.class);
        armorStand.setVisible(false);

        armorStand.customName(Component.text(round(damageDealt, 2), TextColor.color(255, 75, 0), TextDecoration.BOLD));
        armorStand.setCustomNameVisible(true);

        return armorStand;
    }

    public void animateArmorStand(Location initialLocation, double damageDealt) {
        ArmorStand armorStand = this.createArmorStand(initialLocation, damageDealt);

        // Define initial velocity (upward motion)
        Vector velocity = new Vector(0, 0.02, 0); // Adjust the y-component for the desired speed

        // Add randomness to velocity
        double randomX = Math.random() * 0.1 - 0.05; // Random value between -0.05 and 0.05
        double randomY = Math.random() * 0.1 - 0.05;
        double randomZ = Math.random() * 0.1 - 0.05;
        velocity.add(new Vector(randomX, randomY, randomZ));

        // Simulate animation using Bukkit's scheduler
        final AtomicInteger steps = new AtomicInteger(150); // Number of animation steps
        int delay = 4; // Delay between animation steps in server ticks (adjust as needed)

        Bukkit.getScheduler().runTaskTimer(Idletd.getInstance(), () -> {
            // Update ArmorStand position
            armorStand.teleport(armorStand.getLocation().add(velocity));

            // Apply gravity (decrease y-component)
            velocity.subtract(new Vector(randomX, 0.002, randomZ));

            int remainingSteps = steps.decrementAndGet();
            if (remainingSteps <= 0) {
                armorStand.remove();
            }
        }, delay, 600);
    }


    private Location calculateHitLocation(Location start, Location end) {
        // Calculate a point along the line of sight
        Vector startVec = start.toVector();
        Vector endVec = end.toVector();

        // Calculate the midpoint between start and end
        Vector midpoint = startVec.add(endVec.subtract(startVec).multiply(0.5));

        // Convert the resulting vector back to a location
        return midpoint.toLocation(start.getWorld());
    }

}

