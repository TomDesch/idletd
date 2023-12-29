package io.github.stealingdapenta.idletd.custommob.mobtypes;

import io.github.stealingdapenta.idletd.plot.Plot;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.EntityType;

public class SkeletonMob extends CustomMob {

    public SkeletonMob(Plot plot, int level) {
        super();
        this.plot = plot;
        this.level = level;
        this.entityType = EntityType.SKELETON;
        this.nameColor = generateNameColor();
    }

    // Goes from light red to dark red based on Level (peaking at Lv. 1000)
    private TextColor generateNameColor() {
        int colorValue = 156 - level / 5;
        colorValue = Math.max(colorValue, 0);
        return TextColor.color(colorValue, colorValue, colorValue);
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

    /**
     * Initializes the attack power of the skeleton based on its level. The attack power starts at 1.0 for a level 1 mob and scales linearly, reaching a maximum
     * of 50.0 at level 1000. Desmos: f\left(x\right)=\min\left(1\ +\ \frac{x\cdot49}{1000},\ 50\right)
     */
    @Override
    protected void initializeAttackPower() {
        int level = getLevel();
        attackPower = Math.min(1.0 + (level) / 1000.0 * 49.0, 50.0);
    }


    /**
     * Initializes the attack range of the skeleton based on its level.
     * The attack range is calculated to be between 10.0 and 30.0, scaling linearly with the level.
     * It is capped at 30.0 when the level exceeds 1000.
     * Desmos: f\left(x\right)=\min\left(10+\left(\frac{x}{1000}\cdot20\right),\ 30\right)
     */
    @Override
    protected void initializeAttackRange() {
        int level = getLevel();
        attackRange = Math.min(10.0 + (level / 1000.0) * 20.0, 30.0);
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