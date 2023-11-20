package io.github.stealingdapenta.idletd.service.custommob;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import java.util.Objects;

public class MobWrapper {
    private static final String NPE_MESSAGE = "Field %s should not be null!";
    private final LivingEntity entity;

    MobWrapper(MobWrapperBuilder builder) {
        entity = (LivingEntity) builder.location.getWorld().spawnEntity(builder.location, builder.entityType);
        entity.setCanPickupItems(false);
        entity.setRemoveWhenFarAway(false);
        setName(builder.name);

        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH), String.format(NPE_MESSAGE, "maxHealth")).setBaseValue(builder.maxHealth);
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED), String.format(NPE_MESSAGE, "speed")).setBaseValue(builder.speed);
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ARMOR), String.format(NPE_MESSAGE, "armor")).setBaseValue(builder.armor);
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE), String.format(NPE_MESSAGE, "attackDamage")).setBaseValue(builder.attackDamage);
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK), String.format(NPE_MESSAGE, "attackKnockback")).setBaseValue(builder.attackKnockback);
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS), String.format(NPE_MESSAGE, "armorToughness")).setBaseValue(builder.armorToughness);
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED), String.format(NPE_MESSAGE, "attackSpeed")).setBaseValue(builder.attackSpeed);
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_FLYING_SPEED), String.format(NPE_MESSAGE, "flyingSpeed")).setBaseValue(builder.flyingSpeed);
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE), String.format(NPE_MESSAGE, "knockbackResistance")).setBaseValue(builder.knockbackResistance);
    }

    private void setName(TextComponent name) {
        entity.customName(name);
        entity.setCustomNameVisible(true);
    }

    // Method to handle mob death during the wave
    public void handleDeath() {
        // Add any logic you need when a mob dies during the wave
        // For example, you might remove the mob from the living mobs list
//        CustomMobHandler.getInstance().removeCustomMob(this);
    }

    // Getter method for the entity
    public LivingEntity getEntity() {
        return entity;
    }
}
