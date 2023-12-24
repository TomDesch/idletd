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
    double movement_speed;
    double max_health;
    double regeneration_per_second;
    double overheal_shield_limit;
    double overheal_shield_regeneration_per_second;
    double knockback_resistance;

    double sword_resistance;
    double axe_resistance;
    double magic_resistance;
    double arrow_resistance;
    double trident_resistance;
    double explosion_resistance;
    double fire_resistance;
    double poison_resistance;
    double critical_hit_resistance;
    double block_chance;

    double attack_power;
    double attack_range;
    double attack_knockback;
    double attack_speed;
    double projectile_speed;
    double critical_hit_chance;
    double critical_hit_damage_multiplier;


    public MobWrapper(MobWrapperBuilder builder) {
        summonedEntity = (LivingEntity) builder.location.getWorld().spawnEntity(builder.location, builder.entityType, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {

            LivingEntity livingEntity = (LivingEntity) entity;

            livingEntity.setCanPickupItems(false);
            livingEntity.setRemoveWhenFarAway(false);
            entity.customName(builder.name);
            entity.setCustomNameVisible(true);

            setAttribute(livingEntity, GENERIC_MAX_HEALTH, builder.max_health);
            setAttribute(livingEntity, GENERIC_MOVEMENT_SPEED, builder.movement_speed);
            setAttribute(livingEntity, GENERIC_ATTACK_KNOCKBACK, attack_knockback);
            setAttribute(livingEntity, GENERIC_KNOCKBACK_RESISTANCE, builder.knockback_resistance);

            livingEntity.getPersistentDataContainer().set(CustomMobHandler.getCustomNameSpacedKey(), PersistentDataType.BOOLEAN, true);
            livingEntity.getPersistentDataContainer().set(CustomMobHandler.getPlayerNameSpacedKey(), PersistentDataType.STRING, builder.playerUUID);

            setAttribute(livingEntity, MOVEMENT_SPEED.getAttributeName(), builder.movement_speed);
            setAttribute(livingEntity, MAX_HEALTH.getAttributeName(), builder.max_health);
            setAttribute(livingEntity, REGENERATION_PER_SECOND.getAttributeName(), builder.regeneration_per_second);
            setAttribute(livingEntity, OVERHEAL_SHIELD_LIMIT.getAttributeName(), builder.overheal_shield_limit);
            setAttribute(livingEntity, OVERHEAL_SHIELD_REGEN_PER_SECOND.getAttributeName(), builder.overheal_shield_regeneration_per_second);
            setAttribute(livingEntity, KNOCKBACK_RESISTANCE.getAttributeName(), builder.knockback_resistance);

            setAttribute(livingEntity, SWORD_RESISTANCE.getAttributeName(), builder.sword_resistance);
            setAttribute(livingEntity, AXE_RESISTANCE.getAttributeName(), builder.axe_resistance);
            setAttribute(livingEntity, MAGIC_RESISTANCE.getAttributeName(), builder.magic_resistance);
            setAttribute(livingEntity, ARROW_RESISTANCE.getAttributeName(), builder.arrow_resistance);
            setAttribute(livingEntity, TRIDENT_RESISTANCE.getAttributeName(), builder.trident_resistance);
            setAttribute(livingEntity, EXPLOSION_RESISTANCE.getAttributeName(), builder.explosion_resistance);
            setAttribute(livingEntity, FIRE_RESISTANCE.getAttributeName(), builder.fire_resistance);
            setAttribute(livingEntity, POISON_RESISTANCE.getAttributeName(), builder.poison_resistance);
            setAttribute(livingEntity, CRITICAL_HIT_RESISTANCE.getAttributeName(), builder.critical_hit_resistance);
            setAttribute(livingEntity, BLOCK_CHANCE.getAttributeName(), builder.block_chance);

            setAttribute(livingEntity, ATTACK_POWER.getAttributeName(), builder.attack_power);
            setAttribute(livingEntity, ATTACK_RANGE.getAttributeName(), builder.attack_range);
            setAttribute(livingEntity, ATTACK_KNOCKBACK.getAttributeName(), builder.attack_knockback);
            setAttribute(livingEntity, ATTACK_SPEED.getAttributeName(), builder.attack_speed);
            setAttribute(livingEntity, PROJECTILE_SPEED.getAttributeName(), builder.projectile_speed);
            setAttribute(livingEntity, CRITICAL_HIT_CHANCE.getAttributeName(), builder.critical_hit_chance);
            setAttribute(livingEntity, CRITICAL_HIT_DAMAGE_MULTIPLIER.getAttributeName(), builder.critical_hit_damage_multiplier);

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
}