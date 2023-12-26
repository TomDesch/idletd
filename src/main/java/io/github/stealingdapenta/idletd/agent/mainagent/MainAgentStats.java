package io.github.stealingdapenta.idletd.agent.mainagent;

import io.github.stealingdapenta.idletd.agent.AgentStats;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MainAgentStats extends AgentStats {

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
