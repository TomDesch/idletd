package io.github.stealingdapenta.idletd.custommob.mobtypes;

import io.github.stealingdapenta.idletd.plot.Plot;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.EntityType;

public class ZombieMob extends CustomMob {
  // Tank mob

  public ZombieMob(Plot plot, int level) {
    super();
    this.plot = plot;
    this.level = level;
    this.entityType = EntityType.ZOMBIE;
    this.nameColor = generateNameColor();
  }

  // Goes from light green to dark green based on Level (peaking at Lv. 1000)
  private TextColor generateNameColor() {
    int colorValue = 255 - level / 4;
    colorValue = Math.max(colorValue, 0);
    return TextColor.color(0, colorValue, 0);
  }


  @Override
  protected void initializeMovementSpeed() {
    movement_speed = 0.2 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeMaxHealth() {
    max_health = 10 + (double) getLevel() / 10;
  }

  @Override
  protected void initializeRegenerationPerSecond() {
    regeneration_per_second = 0.1 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeOverhealShieldLimit() {
    overheal_shield_limit = 1.0 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeOverhealShieldRegenerationPerSecond() {
    overheal_shield_regeneration_per_second = 0.05 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeKnockbackResistance() {
    knockback_resistance = 0.1 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeSwordResistance() {
    sword_resistance = 0.1 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeAxeResistance() {
    axe_resistance = 0.1 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeMagicResistance() {
    magic_resistance = 0.1 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeArrowResistance() {
    arrow_resistance = 0.1 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeTridentResistance() {
    trident_resistance = 0.1 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeExplosionResistance() {
    explosion_resistance = 0.1 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeFireResistance() {
    fire_resistance = 0.1 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializePoisonResistance() {
    poison_resistance = 0.1 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeCriticalHitResistance() {
    critical_hit_resistance = 0.1 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeBlockChance() {
    block_chance = 0 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeAttackPower() {
    attack_power = 1.0 + (double) getLevel() / 100;
  }

  @Override
  protected void initializeAttackRange() {
    attack_range = 1.0 + (double) getLevel() / 100;
  }

  @Override
  protected void initializeAttackKnockback() {
    attack_knockback = 0 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeAttackSpeed() {
    attack_speed = 0.5 + ((double) getLevel() / 1000);
  }

  @Override
  protected void initializeProjectileSpeed() {
    projectile_speed = 1.0 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeCriticalHitChance() {
    critical_hit_chance = 0.01 + (double) getLevel() / 1000;
  }

  @Override
  protected void initializeCriticalHitDamageMultiplier() {
    critical_hit_damage_multiplier = 1.1 + (double) getLevel() / 1000;
  }

}
