package io.github.stealingdapenta.idletd.custommob;

import io.github.stealingdapenta.idletd.Idletd;
import java.util.Objects;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@Getter
public enum MobAttributes {
    MOVEMENT_SPEED("movement_speed"),
    MAX_HEALTH("max_health"),
    REGENERATION_PER_SECOND("regeneration_per_second"),
    OVERHEAL_SHIELD_LIMIT("overheal_shield_limit"),
    OVERHEAL_SHIELD_REGEN_PER_SECOND("overheal_shield_regeneration_per_second"),
    KNOCKBACK_RESISTANCE("knockback_resistance"),
    SWORD_RESISTANCE("sword_resistance"),
    AXE_RESISTANCE("axe_resistance"),
    MAGIC_RESISTANCE("magic_resistance"),
    ARROW_RESISTANCE("arrow_resistance"),
    TRIDENT_RESISTANCE("trident_resistance"),
    EXPLOSION_RESISTANCE("explosion_resistance"),
    FIRE_RESISTANCE("fire_resistance"),
    POISON_RESISTANCE("poison_resistance"),
    CRITICAL_HIT_RESISTANCE("critical_hit_resistance"),
    BLOCK_CHANCE("block_chance"),
    ATTACK_POWER("attack_power"),
    ATTACK_RANGE("attack_range"),
    ATTACK_KNOCKBACK("attack_knockback"),
    ATTACK_SPEED("attack_speed"),
    PROJECTILE_SPEED("projectile_speed"),
    CRITICAL_HIT_CHANCE("critical_hit_chance"),
    CRITICAL_HIT_DAMAGE_MULTIPLIER("critical_hit_damage_multiplier");

    private static final String ENTITY_DOESNT_HAVE_ATTRIBUTE = "Couldn't find attribute %s for entity %s";
    private final String attributeName;

    MobAttributes(String attributeName) {
        this.attributeName = attributeName;
    }

    public double getValueFor(LivingEntity livingEntity) {
        NamespacedKey attributeKey = new NamespacedKey(Idletd.getInstance(), getAttributeName());
        PersistentDataContainer container = livingEntity.getPersistentDataContainer();
        Double possibleValue = container.get(attributeKey, PersistentDataType.DOUBLE);

        if (Objects.isNull(possibleValue)) {
            Idletd.logger.warning(ENTITY_DOESNT_HAVE_ATTRIBUTE.formatted(getAttributeName(), livingEntity.getName()));
            possibleValue = 0.0;
        }

        return possibleValue;
    }
}
