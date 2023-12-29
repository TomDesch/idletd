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
        double baseSpeed = 0.2;
        double maxSpeed = 1.0;

        movementSpeed = Math.min(maxSpeed, baseSpeed + (level > 250 ? 0 : (level - 1) * (maxSpeed - baseSpeed) / 249.0));
    }

    @Override
    protected void initializeMaxHealth() {
        maxHealth = 10 + (double) getLevel() / 10;
    }

    @Override
    protected void initializeRegenerationPerSecond() {
        regenerationPerSecond = 0.1 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeOverhealShieldLimit() {
        overhealShieldLimit = 1.0 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeOverhealShieldRegenerationPerSecond() {
        overhealShieldRegenerationPerSecond = 0.05 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeKnockbackResistance() {
        knockbackResistance = 0.1 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeSwordResistance() {
        swordResistance = 0.1 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeAxeResistance() {
        axeResistance = 0.1 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeMagicResistance() {
        magicResistance = 0.1 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeArrowResistance() {
        arrowResistance = 0.1 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeTridentResistance() {
        tridentResistance = 0.1 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeExplosionResistance() {
        explosionResistance = 0.1 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeFireResistance() {
        fireResistance = 0.1 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializePoisonResistance() {
        poisonResistance = 0.1 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeCriticalHitResistance() {
        criticalHitResistance = 0.1 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeBlockChance() {
        blockChance = 0 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeAttackPower() {
        attackPower = 1.0 + (double) getLevel() / 100;
    }

    @Override
    protected void initializeAttackRange() {
        attackRange = 1.0 + (double) getLevel() / 100;
    }

    @Override
    protected void initializeAttackKnockback() {
        attackKnockback = 0 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeAttackSpeed() {
        attackSpeed = 0.5 + ((double) getLevel() / 1000);
    }

    @Override
    protected void initializeProjectileSpeed() {
        projectileSpeed = 1.0 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeCriticalHitChance() {
        criticalHitChance = 0.01 + (double) getLevel() / 1000;
    }

    @Override
    protected void initializeCriticalHitDamageMultiplier() {
        criticalHitDamageMultiplier = 1.1 + (double) getLevel() / 1000;
    }

}
