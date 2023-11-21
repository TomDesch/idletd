package io.github.stealingdapenta.idletd.service.custommob;

import io.github.stealingdapenta.idletd.Idletd;
import lombok.Getter;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

import static org.bukkit.attribute.Attribute.GENERIC_ARMOR;
import static org.bukkit.attribute.Attribute.GENERIC_ARMOR_TOUGHNESS;
import static org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE;
import static org.bukkit.attribute.Attribute.GENERIC_ATTACK_KNOCKBACK;
import static org.bukkit.attribute.Attribute.GENERIC_KNOCKBACK_RESISTANCE;
import static org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH;
import static org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED;

@Getter
public class MobWrapper {
    private final LivingEntity summonedEntity;

    MobWrapper(MobWrapperBuilder builder) {
        CustomMobHandler customMobHandler = CustomMobHandler.getInstance();

        summonedEntity = (LivingEntity) builder.location.getWorld().spawnEntity(builder.location, builder.entityType, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {

            LivingEntity livingEntity = (LivingEntity) entity;

            livingEntity.setCanPickupItems(false);
            livingEntity.setRemoveWhenFarAway(false);
            entity.customName(builder.name);
            entity.setCustomNameVisible(true);

            setAttribute(livingEntity, GENERIC_MAX_HEALTH, builder.maxHealth);
            setAttribute(livingEntity, GENERIC_MOVEMENT_SPEED, builder.speed);
            setAttribute(livingEntity, GENERIC_ARMOR, builder.armor);
            setAttribute(livingEntity, GENERIC_ATTACK_DAMAGE, builder.attackDamage);
            setAttribute(livingEntity, GENERIC_ATTACK_KNOCKBACK, builder.attackKnockback);
            setAttribute(livingEntity, GENERIC_ARMOR_TOUGHNESS, builder.armorToughness);
            setAttribute(livingEntity, GENERIC_KNOCKBACK_RESISTANCE, builder.knockbackResistance);

            livingEntity.getPersistentDataContainer().set(customMobHandler.getCustomNameSpacedKey(), PersistentDataType.BOOLEAN, true);
            livingEntity.getPersistentDataContainer().set(customMobHandler.getPlayerNameSpacedKey(), PersistentDataType.STRING, builder.playerUUID);
        });
    }

    private void setAttribute(LivingEntity entity, Attribute attribute, double value) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (Objects.nonNull(attributeInstance)) {
            attributeInstance.setBaseValue(value);
        } else {
            Idletd.getInstance().getLogger().severe("The attribute " + attribute.name() + " is null for " + entity.getName() + ".");
        }
    }
}