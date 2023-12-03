package io.github.stealingdapenta.idletd.service.utils;

import io.github.stealingdapenta.idletd.custommob.CustomMobHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Comparator;

@RequiredArgsConstructor
public class EntityTracker {
    private final CustomMobHandler customMobHandler;

    // todo add a setting in the future where the player can choose whether the agent targets:
    // the closest / furthest / healthiest / weakest enemy

    private LivingEntity getNearestEnemy(Location location, int radius) {
        return getExtremeEntity(location, radius, Comparator.naturalOrder());
    }

    private LivingEntity getFurthestEnemy(Location location, int radius) {
        return getExtremeEntity(location, radius, Comparator.reverseOrder());
    }

    private LivingEntity getEntityWithMostHealth(Location location, int radius) {
        return getExtremeEntity(location, radius, Comparator.naturalOrder());
    }

    private LivingEntity getEntityWithLeastHealth(Location location, int radius) {
        return getExtremeEntity(location, radius, Comparator.reverseOrder());
    }

    private LivingEntity getExtremeEntity(Location location, int radius, Comparator<Double> comparator) {
        LivingEntity extremeEntity = null;
        double extremeValue = comparator.equals(Comparator.naturalOrder()) ? Double.MAX_VALUE : 0.0;

        World world = location.getWorld();

        for (Entity entity : world.getNearbyEntities(location, radius, radius, radius)) {
            if (entity instanceof LivingEntity livingEntity && customMobHandler.isCustomMob(livingEntity)) {
                double value = 0.0;

                if (comparator.equals(Comparator.naturalOrder())) {
                    value = livingEntity.getHealth();
                } else if (comparator.equals(Comparator.reverseOrder())) {
                    value = location.distanceSquared(entity.getLocation());
                }

                if (comparator.compare(value, extremeValue) > 0) {
                    extremeValue = value;
                    extremeEntity = livingEntity;
                }
            }
        }

        return extremeEntity;
    }
}
