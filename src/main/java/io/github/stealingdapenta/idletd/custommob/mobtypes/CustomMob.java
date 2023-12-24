package io.github.stealingdapenta.idletd.custommob.mobtypes;

import static io.github.stealingdapenta.idletd.custommob.CustomMobHandler.getPlayerNameSpacedKey;
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
import static io.github.stealingdapenta.idletd.service.utils.Time.ONE_SECOND_IN_TICKS;
import static io.github.stealingdapenta.idletd.service.utils.Time.ZERO_TICKS;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.custommob.CustomMobHandler;
import io.github.stealingdapenta.idletd.custommob.MobWrapper;
import io.github.stealingdapenta.idletd.plot.Plot;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
@Setter
@Getter
public abstract class CustomMob {

    protected EntityType entityType;
    protected Mob mob;
    protected Plot plot;
    protected int level;

    protected double movement_speed = 0.2; // 0.2 = default zombie speed

    protected double max_health = 10.0;
    protected double regeneration_per_second = 1.0;
    protected double overheal_shield_limit = 1.0;
    protected double overheal_shield_regeneration_per_second = 1.0;
    protected double knockback_resistance = 1.0;

    protected double sword_resistance = 1.0;
    protected double axe_resistance = 1.0;
    protected double magic_resistance = 1.0;
    protected double arrow_resistance = 1.0;
    protected double trident_resistance = 1.0;
    protected double explosion_resistance = 1.0;
    protected double fire_resistance = 1.0;
    protected double poison_resistance = 1.0;
    protected double critical_hit_resistance = 1.0;
    protected double block_chance = 1.0;

    protected double attack_power = 1.0;
    protected double attack_range = 1.0;
    protected double attack_knockback = 1.0;
    protected double attack_speed = 1.0;
    protected double projectile_speed = 1.0;
    protected double critical_hit_chance = 1.0;
    protected double critical_hit_damage_multiplier = 1.0;


    protected TextColor nameColor = TextColor.color(146, 9, 9);

    public static NamespacedKey getLevelNSK() {
        return new NamespacedKey(Idletd.getInstance(), "level");
    }

    public static MobWrapper createFrom(LivingEntity livingEntity) {
        return MobWrapper.builder()
                         .playerUUID(livingEntity.getPersistentDataContainer()
                                                 .get(getPlayerNameSpacedKey(), PersistentDataType.STRING))
                         .location(livingEntity.getLocation())
                         .name(Objects.nonNull(livingEntity.customName()) ? (TextComponent) livingEntity.customName() : Component.text(livingEntity.getName()))
                         .entityType(livingEntity.getType())
                         .movement_speed(MOVEMENT_SPEED.getValueFor(livingEntity))
                         .max_health(MAX_HEALTH.getValueFor(livingEntity))
                         .regeneration_per_second(REGENERATION_PER_SECOND.getValueFor(livingEntity))
                         .overheal_shield_limit(OVERHEAL_SHIELD_LIMIT.getValueFor(livingEntity))
                         .overheal_shield_regeneration_per_second(OVERHEAL_SHIELD_REGEN_PER_SECOND.getValueFor(livingEntity))
                         .knockback_resistance(KNOCKBACK_RESISTANCE.getValueFor(livingEntity))
                         .sword_resistance(SWORD_RESISTANCE.getValueFor(livingEntity))
                         .axe_resistance(AXE_RESISTANCE.getValueFor(livingEntity))
                         .magic_resistance(MAGIC_RESISTANCE.getValueFor(livingEntity))
                         .arrow_resistance(ARROW_RESISTANCE.getValueFor(livingEntity))
                         .trident_resistance(TRIDENT_RESISTANCE.getValueFor(livingEntity))
                         .explosion_resistance(EXPLOSION_RESISTANCE.getValueFor(livingEntity))
                         .fire_resistance(FIRE_RESISTANCE.getValueFor(livingEntity))
                         .poison_resistance(POISON_RESISTANCE.getValueFor(livingEntity))
                         .critical_hit_resistance(CRITICAL_HIT_RESISTANCE.getValueFor(livingEntity))
                         .block_chance(BLOCK_CHANCE.getValueFor(livingEntity))
                         .attack_power(ATTACK_POWER.getValueFor(livingEntity))
                         .attack_range(ATTACK_RANGE.getValueFor(livingEntity))
                         .attack_knockback(ATTACK_KNOCKBACK.getValueFor(livingEntity))
                         .attack_speed(ATTACK_SPEED.getValueFor(livingEntity))
                         .projectile_speed(PROJECTILE_SPEED.getValueFor(livingEntity))
                         .critical_hit_chance(CRITICAL_HIT_CHANCE.getValueFor(livingEntity))
                         .critical_hit_damage_multiplier(CRITICAL_HIT_DAMAGE_MULTIPLIER.getValueFor(livingEntity))
                         .build();
    }

    public Mob summon(Location location) {
        initializeMovementSpeed();
        initializeMaxHealth();
        initializeRegenerationPerSecond();
        initializeOverhealShieldLimit();
        initializeOverhealShieldRegenerationPerSecond();
        initializeKnockbackResistance();
        initializeSwordResistance();
        initializeAxeResistance();
        initializeMagicResistance();
        initializeArrowResistance();
        initializeTridentResistance();
        initializeExplosionResistance();
        initializeFireResistance();
        initializePoisonResistance();
        initializeCriticalHitResistance();
        initializeBlockChance();
        initializeAttackPower();
        initializeAttackRange();
        initializeAttackKnockback();
        initializeAttackSpeed();
        initializeProjectileSpeed();
        initializeCriticalHitChance();
        initializeCriticalHitDamageMultiplier();

        MobWrapper customMob = MobWrapper.builder()
                                         .playerUUID(Objects.nonNull(plot) ? plot.getPlayerUUID() : null)
                                         .location(location)
                                         .name(generateMobName())
                                         .entityType(entityType)
                                         .movement_speed(movement_speed)
                                         .max_health(max_health)
                                         .regeneration_per_second(regeneration_per_second)
                                         .overheal_shield_limit(overheal_shield_limit)
                                         .overheal_shield_regeneration_per_second(overheal_shield_regeneration_per_second)
                                         .knockback_resistance(knockback_resistance)
                                         .sword_resistance(sword_resistance)
                                         .axe_resistance(axe_resistance)
                                         .magic_resistance(magic_resistance)
                                         .arrow_resistance(arrow_resistance)
                                         .trident_resistance(trident_resistance)
                                         .explosion_resistance(explosion_resistance)
                                         .fire_resistance(fire_resistance)
                                         .poison_resistance(poison_resistance)
                                         .critical_hit_resistance(critical_hit_resistance)
                                         .block_chance(block_chance)
                                         .attack_power(attack_power)
                                         .attack_range(attack_range)
                                         .attack_knockback(attack_knockback)
                                         .attack_speed(attack_speed)
                                         .projectile_speed(projectile_speed)
                                         .critical_hit_chance(critical_hit_chance)
                                         .critical_hit_damage_multiplier(critical_hit_damage_multiplier)
                                         .build();

        mob = (Mob) new CustomMobHandler().spawnCustomMob(customMob).getSummonedEntity();

        // todo set Target to the main agent of the plot (we have this.plot)

        preventMobFromFallingTask();

        mob.getPersistentDataContainer().set(getLevelNSK(), PersistentDataType.INTEGER, getLevel());

        return mob;
    }

    private TextComponent generateMobName() {
        String name = getEntityType().name() + " [Lv." + getLevel() + "]";
        return Component.text(name, getNameColor())
                        .toBuilder()
                        .build();
    }

    protected abstract void initializeMovementSpeed();

    protected abstract void initializeMaxHealth();

    protected abstract void initializeRegenerationPerSecond();

    protected abstract void initializeOverhealShieldLimit();

    protected abstract void initializeOverhealShieldRegenerationPerSecond();

    protected abstract void initializeKnockbackResistance();

    protected abstract void initializeSwordResistance();

    protected abstract void initializeAxeResistance();

    protected abstract void initializeMagicResistance();

    protected abstract void initializeArrowResistance();

    protected abstract void initializeTridentResistance();

    protected abstract void initializeExplosionResistance();

    protected abstract void initializeFireResistance();

    protected abstract void initializePoisonResistance();

    protected abstract void initializeCriticalHitResistance();

    protected abstract void initializeBlockChance();

    protected abstract void initializeAttackPower();

    protected abstract void initializeAttackRange();

    protected abstract void initializeAttackKnockback();

    protected abstract void initializeAttackSpeed();

    protected abstract void initializeProjectileSpeed();

    protected abstract void initializeCriticalHitChance();

    protected abstract void initializeCriticalHitDamageMultiplier();

    private void preventMobFromFallingTask() { // todo in the future have one task for all mobs.
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Objects.isNull(plot) || !mob.isValid()) {
                    cancel();
                }

                if (mob.getFallDistance() > 5) {
                    mob.setFallDistance(0); // prevents fall dmg
                    mob.teleport(plot.getMobSpawnLocation());
                }
            }
        }.runTaskTimer(Idletd.getInstance(), ZERO_TICKS, 2 * ONE_SECOND_IN_TICKS);
    }

}
