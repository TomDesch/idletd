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
    MOVEMENT_SPEED("movementSpeed"),
    MAX_HEALTH("maxHealth"),
    REGENERATION_PER_SECOND("regenerationPerSecond"),
    OVERHEAL_SHIELD_LIMIT("overhealShieldLimit"),
    OVERHEAL_SHIELD_REGEN_PER_SECOND("overhealShieldRegenerationPerSecond"),
    KNOCKBACK_RESISTANCE("knockbackResistance"),
    SWORD_RESISTANCE("swordResistance"),
    AXE_RESISTANCE("axeResistance"),
    MAGIC_RESISTANCE("magicResistance"),
    ARROW_RESISTANCE("arrowResistance"),
    TRIDENT_RESISTANCE("tridentResistance"),
    EXPLOSION_RESISTANCE("explosionResistance"),
    FIRE_RESISTANCE("fireResistance"),
    POISON_RESISTANCE("poisonResistance"),
    CRITICAL_HIT_RESISTANCE("criticalHitResistance"),
    BLOCK_CHANCE("blockChance"),
    ATTACK_POWER("attackPower"),
    ATTACK_RANGE("attackRange"),
    ATTACK_KNOCKBACK("attackKnockback"),
    ATTACK_SPEED("attackSpeed"),
    PROJECTILE_SPEED("projectileSpeed"),
    CRITICAL_HIT_CHANCE("criticalHitChance"),
    CRITICAL_HIT_DAMAGE_MULTIPLIER("criticalHitDamageMultiplier");

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
