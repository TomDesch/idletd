package io.github.stealingdapenta.idletd.custommob.mobtypes;

import io.github.stealingdapenta.idletd.agent.Agent;
import io.github.stealingdapenta.idletd.custommob.CustomMobGoal;
import io.github.stealingdapenta.idletd.plot.Plot;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

public class SkeletonMob extends CustomMob {

    public SkeletonMob(Plot plot, int level) {
        super();
        this.plot = plot;
        this.level = level;
        this.entityType = EntityType.SKELETON;
        this.nameColor = generateNameColor();
    }

    @Override
    public Mob summon(Location location, Agent agent) {
        Mob mob = super.summon(location, agent);

        // todo this does not fix the attack speed animation issue
        Bukkit.getMobGoals()
              .addGoal(mob, 0, new CustomMobGoal(mob, agent));
        return mob;
    }

    // Goes from light red to dark red based on Level (peaking at Lv. 1000)
    private TextColor generateNameColor() {
        int colorValue = 156 - level / 5;
        colorValue = Math.max(colorValue, 0);
        return TextColor.color(colorValue, colorValue, colorValue);
    }

    /**
     * Initializes the movement speed based on the character's level. Uses a formula to calculate the movement speed with a base and maximum speed. Formula:
     * speed = min(maxSpeed, baseSpeed + level * (maxSpeed - baseSpeed) / 500) desmos: f\left(x\right)=\min\left(0.2\ +\ \frac{x\cdot\left(1-0.2\right)}{500},\
     * 1.0\right)
     */
    @Override
    protected void initializeMovementSpeed() {
        double baseSpeed = 0.2;
        double maxSpeed = 1.0;
        movementSpeed = Math.min(maxSpeed, baseSpeed + level * (maxSpeed - baseSpeed) / 500);
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

    /**
     * Initializes the attack speed based on the level of the entity. The speed is capped at its maximum value of 10.0 when the entity's level exceeds 1107 (123
     * * 10). Desmos: f\left(x\right)\ =\ \min\left(10,\ 1+\left(\frac{x}{123}\right)\right)
     */
    @Override
    protected void initializeAttackSpeed() {
        attackSpeed = Math.min(10.0, 1.0 + (getLevel() / 123.0));
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