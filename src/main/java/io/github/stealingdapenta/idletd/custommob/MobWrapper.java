package io.github.stealingdapenta.idletd.custommob;

import static io.github.stealingdapenta.idletd.custommob.MobAttributes.ARROW_RESISTANCE;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.ATTACK_KNOCKBACK;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.ATTACK_POWER;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.ATTACK_RANGE;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.ATTACK_SPEED;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.AXE_RESISTANCE;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.BLOCK_CHANCE;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.CRITICAL_HIT_CHANCE;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.CRITICAL_HIT_DAMAGE_MULTIPLIER;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.CRITICAL_HIT_RESISTANCE;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.EXPLOSION_RESISTANCE;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.FIRE_RESISTANCE;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.KNOCKBACK_RESISTANCE;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.MAGIC_RESISTANCE;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.MAX_HEALTH;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.MOVEMENT_SPEED;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.OVERHEAL_SHIELD_LIMIT;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.OVERHEAL_SHIELD_REGEN_PER_SECOND;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.POISON_RESISTANCE;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.PROJECTILE_SPEED;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.REGENERATION_PER_SECOND;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.SWORD_RESISTANCE;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.TRIDENT_RESISTANCE;
import static org.bukkit.attribute.Attribute.GENERIC_ATTACK_KNOCKBACK;
import static org.bukkit.attribute.Attribute.GENERIC_KNOCKBACK_RESISTANCE;
import static org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH;
import static org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED;

import io.github.stealingdapenta.idletd.Idletd;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@Getter
@Builder
@AllArgsConstructor
public class MobWrapper {
    private final LivingEntity summonedEntity;
    String playerUUID;
    EntityType entityType;
    Location location;
    TextComponent name;
    double movementSpeed;
    double maxHealth;
    double regenerationPerSecond;
    double overhealShieldLimit;
    double overhealShieldRegenerationPerSecond;
    double knockbackResistance;

    double swordResistance;
    double axeResistance;
    double magicResistance;
    double arrowResistance;
    double tridentResistance;
    double explosionResistance;
    double fireResistance;
    double poisonResistance;
    double criticalHitResistance;
    double blockChance;

    double attackPower;
    double attackRange;
    double attackKnockback;
    double attackSpeed;
    double projectileSpeed;
    double criticalHitChance;
    double criticalHitDamageMultiplier;


    public MobWrapper(MobWrapperBuilder builder) {
        summonedEntity = (LivingEntity) builder.location.getWorld().spawnEntity(builder.location, builder.entityType, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {

            LivingEntity livingEntity = (LivingEntity) entity;

            livingEntity.setCanPickupItems(false);
            livingEntity.setRemoveWhenFarAway(false);
            entity.customName(builder.name);
            entity.setCustomNameVisible(true);

            setAttribute(livingEntity, GENERIC_MAX_HEALTH, builder.maxHealth);
            setAttribute(livingEntity, GENERIC_MOVEMENT_SPEED, builder.movementSpeed);
            setAttribute(livingEntity, GENERIC_ATTACK_KNOCKBACK, attackKnockback);
            setAttribute(livingEntity, GENERIC_KNOCKBACK_RESISTANCE, builder.knockbackResistance);

            livingEntity.getPersistentDataContainer().set(CustomMobHandler.getCustomNameSpacedKey(), PersistentDataType.BOOLEAN, true);
            livingEntity.getPersistentDataContainer().set(CustomMobHandler.getPlayerNameSpacedKey(), PersistentDataType.STRING, builder.playerUUID);

            setAttribute(livingEntity, MOVEMENT_SPEED.getAttributeName(), builder.movementSpeed);
            setAttribute(livingEntity, MAX_HEALTH.getAttributeName(), builder.maxHealth);
            setAttribute(livingEntity, REGENERATION_PER_SECOND.getAttributeName(), builder.regenerationPerSecond);
            setAttribute(livingEntity, OVERHEAL_SHIELD_LIMIT.getAttributeName(), builder.overhealShieldLimit);
            setAttribute(livingEntity, OVERHEAL_SHIELD_REGEN_PER_SECOND.getAttributeName(), builder.overhealShieldRegenerationPerSecond);
            setAttribute(livingEntity, KNOCKBACK_RESISTANCE.getAttributeName(), builder.knockbackResistance);

            setAttribute(livingEntity, SWORD_RESISTANCE.getAttributeName(), builder.swordResistance);
            setAttribute(livingEntity, AXE_RESISTANCE.getAttributeName(), builder.axeResistance);
            setAttribute(livingEntity, MAGIC_RESISTANCE.getAttributeName(), builder.magicResistance);
            setAttribute(livingEntity, ARROW_RESISTANCE.getAttributeName(), builder.arrowResistance);
            setAttribute(livingEntity, TRIDENT_RESISTANCE.getAttributeName(), builder.tridentResistance);
            setAttribute(livingEntity, EXPLOSION_RESISTANCE.getAttributeName(), builder.explosionResistance);
            setAttribute(livingEntity, FIRE_RESISTANCE.getAttributeName(), builder.fireResistance);
            setAttribute(livingEntity, POISON_RESISTANCE.getAttributeName(), builder.poisonResistance);
            setAttribute(livingEntity, CRITICAL_HIT_RESISTANCE.getAttributeName(), builder.criticalHitResistance);
            setAttribute(livingEntity, BLOCK_CHANCE.getAttributeName(), builder.blockChance);

            setAttribute(livingEntity, ATTACK_POWER.getAttributeName(), builder.attackPower);
            setAttribute(livingEntity, ATTACK_RANGE.getAttributeName(), builder.attackRange);
            setAttribute(livingEntity, ATTACK_KNOCKBACK.getAttributeName(), builder.attackKnockback);
            setAttribute(livingEntity, ATTACK_SPEED.getAttributeName(), builder.attackSpeed);
            setAttribute(livingEntity, PROJECTILE_SPEED.getAttributeName(), builder.projectileSpeed);
            setAttribute(livingEntity, CRITICAL_HIT_CHANCE.getAttributeName(), builder.criticalHitChance);
            setAttribute(livingEntity, CRITICAL_HIT_DAMAGE_MULTIPLIER.getAttributeName(), builder.criticalHitDamageMultiplier);

        });
    }

    public NamespacedKey getNameSpacedKey(String keyName) {
        return new NamespacedKey(Idletd.getInstance(), keyName);
    }

    private void setAttribute(LivingEntity entity, String attributeName, double value) {
        NamespacedKey key = getNameSpacedKey(attributeName);
        PersistentDataContainer container = entity.getPersistentDataContainer();
        container.set(key, PersistentDataType.DOUBLE, value);
    }

    private void setAttribute(LivingEntity entity, Attribute attribute, double value) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (Objects.nonNull(attributeInstance)) {
            attributeInstance.setBaseValue(value);
        } else {
            Idletd.getInstance().getLogger().severe("The attribute " + attribute.name() + " is null for " + entity.getName() + ".");
        }
    }

    public double getAttackRangeSquared() {
        return attackRange * attackRange;
    }
}