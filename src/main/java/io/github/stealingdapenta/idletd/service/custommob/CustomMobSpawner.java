package io.github.stealingdapenta.idletd.service.custommob;

import io.github.stealingdapenta.idletd.Idletd;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

@RequiredArgsConstructor
public class CustomMobSpawner {

    private final CustomMobHandler customMobHandler;

    public void spawnInFrontOfPlayer(EntityType entityType, Player player, int distance) {
        Location loc = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize().multiply(distance);
        Location front = loc.add(direction);

        this.spawn(entityType, front);
    }

    public void spawn(EntityType entityType, Location location) {
        LivingEntity livingEntity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
        this.customMobHandler.addCustomMob(livingEntity);

        livingEntity.setCanPickupItems(false);
        livingEntity.setRemoveWhenFarAway(false);

        NamespacedKey namespacedKey = new NamespacedKey(Idletd.getInstance(), this.customMobHandler.getCUSTOM_MOB_TAG());
        livingEntity.getPersistentDataContainer().set(namespacedKey, PersistentDataType.BOOLEAN, true);

        this.setName(livingEntity);
    }

    private void setName(LivingEntity entity) {
        entity.customName(Component.text("Custom mob", TextColor.color(146, 143, 25)).toBuilder().build());
        entity.setCustomNameVisible(true);
    }
}
