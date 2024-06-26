package io.github.stealingdapenta.idletd.agent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class AgentStats {

    private long id;

    private double maxHealth;
    private double regenerationPerSecond;
    private double overhealShieldLimit;
    private double overhealShieldRegenerationPerSecond;
    private double knockbackResistance;

    private double swordResistance;
    private double axeResistance;
    private double magicResistance;
    private double arrowResistance;
    private double tridentResistance;
    private double explosionResistance;
    private double fireResistance;
    private double poisonResistance;
    private double criticalHitResistance;
    private double blockChance;

    private double attackPower;
    private double attackRange;
    private double attackKnockback;
    private double attackSpeed;
    private double projectileSpeed;
    private double criticalHitChance;
    private double criticalHitDamageMultiplier;

    @Setter
    private double currentHealth;
}
