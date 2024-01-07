package io.github.stealingdapenta.idletd.custommob.mobtypes;

import static io.github.stealingdapenta.idletd.custommob.AttackType.fromString;
import static io.github.stealingdapenta.idletd.custommob.CustomMobHandler.getPlayerNameSpacedKey;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.ARROW_RESISTANCE;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.ATTACK_KNOCKBACK;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.ATTACK_POWER;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.ATTACK_RANGE;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.ATTACK_SPEED;
import static io.github.stealingdapenta.idletd.custommob.MobAttributes.ATTACK_TYPE;
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
import io.github.stealingdapenta.idletd.agent.Agent;
import io.github.stealingdapenta.idletd.custommob.AttackType;
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
    protected AttackType attackType;

    protected double movementSpeed = 0.2; // 0.2 = default zombie speed

    protected double maxHealth = 10.0;
    protected double regenerationPerSecond = 1.0;
    protected double overhealShieldLimit = 1.0;
    protected double overhealShieldRegenerationPerSecond = 1.0;
    protected double knockbackResistance = 1.0;

    protected double swordResistance = 1.0;
    protected double axeResistance = 1.0;
    protected double magicResistance = 1.0;
    protected double arrowResistance = 1.0;
    protected double tridentResistance = 1.0;
    protected double explosionResistance = 1.0;
    protected double fireResistance = 1.0;
    protected double poisonResistance = 1.0;
    protected double criticalHitResistance = 1.0;
    protected double blockChance = 1.0;

    protected double attackPower = 1.0;
    protected double attackRange = 1.0;
    protected double attackKnockback = 1.0;
    protected double attackSpeed = 1.0;
    protected double projectileSpeed = 1.0;
    protected double criticalHitChance = 1.0;
    protected double criticalHitDamageMultiplier = 1.0;


    protected TextColor nameColor = TextColor.color(146, 9, 9);

    public static NamespacedKey getLevelNSK() {
        return new NamespacedKey(Idletd.getInstance(), "level");
    }

    public static MobWrapper createFrom(LivingEntity livingEntity) {
        return MobWrapper.builder()
                         .playerUUID(livingEntity.getPersistentDataContainer()
                                                 .get(getPlayerNameSpacedKey(), PersistentDataType.STRING))
                         .summonedEntity(livingEntity)
                         .location(livingEntity.getLocation())
                         .name(Objects.nonNull(livingEntity.customName()) ? (TextComponent) livingEntity.customName() : Component.text(livingEntity.getName()))
                         .entityType(livingEntity.getType())
                         .movementSpeed(MOVEMENT_SPEED.getValueFor(livingEntity))
                         .maxHealth(MAX_HEALTH.getValueFor(livingEntity))
                         .regenerationPerSecond(REGENERATION_PER_SECOND.getValueFor(livingEntity))
                         .overhealShieldLimit(OVERHEAL_SHIELD_LIMIT.getValueFor(livingEntity))
                         .overhealShieldRegenerationPerSecond(OVERHEAL_SHIELD_REGEN_PER_SECOND.getValueFor(livingEntity))
                         .knockbackResistance(KNOCKBACK_RESISTANCE.getValueFor(livingEntity))
                         .swordResistance(SWORD_RESISTANCE.getValueFor(livingEntity))
                         .axeResistance(AXE_RESISTANCE.getValueFor(livingEntity))
                         .magicResistance(MAGIC_RESISTANCE.getValueFor(livingEntity))
                         .arrowResistance(ARROW_RESISTANCE.getValueFor(livingEntity))
                         .tridentResistance(TRIDENT_RESISTANCE.getValueFor(livingEntity))
                         .explosionResistance(EXPLOSION_RESISTANCE.getValueFor(livingEntity))
                         .fireResistance(FIRE_RESISTANCE.getValueFor(livingEntity))
                         .poisonResistance(POISON_RESISTANCE.getValueFor(livingEntity))
                         .criticalHitResistance(CRITICAL_HIT_RESISTANCE.getValueFor(livingEntity))
                         .blockChance(BLOCK_CHANCE.getValueFor(livingEntity))
                         .attackPower(ATTACK_POWER.getValueFor(livingEntity))
                         .attackRange(ATTACK_RANGE.getValueFor(livingEntity))
                         .attackKnockback(ATTACK_KNOCKBACK.getValueFor(livingEntity))
                         .attackSpeed(ATTACK_SPEED.getValueFor(livingEntity))
                         .projectileSpeed(PROJECTILE_SPEED.getValueFor(livingEntity))
                         .criticalHitChance(CRITICAL_HIT_CHANCE.getValueFor(livingEntity))
                         .criticalHitDamageMultiplier(CRITICAL_HIT_DAMAGE_MULTIPLIER.getValueFor(livingEntity))
                         .attackType(fromString(ATTACK_TYPE.getStringValueFor(livingEntity)))
                         .build();
    }

    public Mob summon(Location location, Agent agent) {
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

        MobWrapper customMob = new MobWrapper(MobWrapper.builder()
                                                        .playerUUID(Objects.nonNull(plot) ? plot.getPlayerUUID() : null)
                                                        .location(location)
                                                        .name(generateMobName())
                                                        .entityType(entityType)
                                                        .movementSpeed(movementSpeed)
                                                        .maxHealth(maxHealth)
                                                        .regenerationPerSecond(regenerationPerSecond)
                                                        .overhealShieldLimit(overhealShieldLimit)
                                                        .overhealShieldRegenerationPerSecond(overhealShieldRegenerationPerSecond)
                                                        .knockbackResistance(knockbackResistance)
                                                        .swordResistance(swordResistance)
                                                        .axeResistance(axeResistance)
                                                        .magicResistance(magicResistance)
                                                        .arrowResistance(arrowResistance)
                                                        .tridentResistance(tridentResistance)
                                                        .explosionResistance(explosionResistance)
                                                        .fireResistance(fireResistance)
                                                        .poisonResistance(poisonResistance)
                                                        .criticalHitResistance(criticalHitResistance)
                                                        .blockChance(blockChance)
                                                        .attackPower(attackPower)
                                                        .attackRange(attackRange)
                                                        .attackKnockback(attackKnockback)
                                                        .attackSpeed(attackSpeed)
                                                        .projectileSpeed(projectileSpeed)
                                                        .criticalHitChance(criticalHitChance)
                                                        .criticalHitDamageMultiplier(criticalHitDamageMultiplier)
                                                        .attackType(attackType));

        new CustomMobHandler().spawnCustomMob(customMob);
        mob = (Mob) customMob.getSummonedEntity();

        preventMobFromFallingTask();

        mob.getPersistentDataContainer()
           .set(getLevelNSK(), PersistentDataType.INTEGER, getLevel());

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
                if (Objects.isNull(plot) || Objects.isNull(mob) || !mob.isValid()) {
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
