package io.github.stealingdapenta.idletd.service.custommob;

import lombok.Getter;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

@Getter
public class MobWrapper {
    private static final String NPE_MESSAGE = "Field %s should not be null!";
    private final LivingEntity summonedEntity;

    MobWrapper(MobWrapperBuilder builder) {
        CustomMobHandler customMobHandler = CustomMobHandler.getInstance();

        summonedEntity = (LivingEntity) builder.location.getWorld().spawnEntity(builder.location, builder.entityType, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {

            LivingEntity livingEntity = (LivingEntity) entity;

            livingEntity.setCanPickupItems(false);
            livingEntity.setRemoveWhenFarAway(false);
            entity.customName(builder.name);
            entity.setCustomNameVisible(true);

            Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH), String.format(NPE_MESSAGE, "maxHealth")).setBaseValue(builder.maxHealth);
            Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED), String.format(NPE_MESSAGE, "speed")).setBaseValue(builder.speed);
            Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_ARMOR), String.format(NPE_MESSAGE, "armor")).setBaseValue(builder.armor);
            Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE), String.format(NPE_MESSAGE, "attackDamage")).setBaseValue(builder.attackDamage);
            Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK), String.format(NPE_MESSAGE, "attackKnockback")).setBaseValue(builder.attackKnockback);
            Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS), String.format(NPE_MESSAGE, "armorToughness")).setBaseValue(builder.armorToughness);
            Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_ATTACK_SPEED), String.format(NPE_MESSAGE, "attackSpeed")).setBaseValue(builder.attackSpeed);
            Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_FLYING_SPEED), String.format(NPE_MESSAGE, "flyingSpeed")).setBaseValue(builder.flyingSpeed);
            Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE), String.format(NPE_MESSAGE, "knockbackResistance")).setBaseValue(builder.knockbackResistance);

            livingEntity.getPersistentDataContainer().set(customMobHandler.getCustomNameSpacedKey(), PersistentDataType.BOOLEAN, true);
        });

    }

    // Method to handle mob death during the wave
    public void handleDeath() {
        // Add any logic you need when a mob dies during the wave
        // For example, you might remove the mob from the living mobs list
//        CustomMobHandler.getInstance().removeCustomMob(this); // death animation
    }

}
