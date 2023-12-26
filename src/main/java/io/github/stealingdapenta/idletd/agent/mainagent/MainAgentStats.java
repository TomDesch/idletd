package io.github.stealingdapenta.idletd.agent.mainagent;

import io.github.stealingdapenta.idletd.agent.AgentStats;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class MainAgentStats extends AgentStats {

    @Builder
    public MainAgentStats(int agentId, double maxHealth, double regenerationPerSecond, double overhealShieldLimit, double overhealShieldRegenerationPerSecond,
            double knockbackResistance, double swordResistance, double axeResistance, double magicResistance, double arrowResistance, double tridentResistance,
            double explosionResistance, double fireResistance, double poisonResistance, double criticalHitResistance, double blockChance, double attackPower,
            double attackRange, double attackKnockback, double attackSpeed, double projectileSpeed, double criticalHitChance,
            double criticalHitDamageMultiplier) {
        super(agentId);
        this.maxHealth = maxHealth;
        this.regenerationPerSecond = regenerationPerSecond;
        this.overhealShieldLimit = overhealShieldLimit;
        this.overhealShieldRegenerationPerSecond = overhealShieldRegenerationPerSecond;
        this.knockbackResistance = knockbackResistance;
        this.swordResistance = swordResistance;
        this.axeResistance = axeResistance;
        this.magicResistance = magicResistance;
        this.arrowResistance = arrowResistance;
        this.tridentResistance = tridentResistance;
        this.explosionResistance = explosionResistance;
        this.fireResistance = fireResistance;
        this.poisonResistance = poisonResistance;
        this.criticalHitResistance = criticalHitResistance;
        this.blockChance = blockChance;
        this.attackPower = attackPower;
        this.attackRange = attackRange;
        this.attackKnockback = attackKnockback;
        this.attackSpeed = attackSpeed;
        this.projectileSpeed = projectileSpeed;
        this.criticalHitChance = criticalHitChance;
        this.criticalHitDamageMultiplier = criticalHitDamageMultiplier;
    }

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
}
