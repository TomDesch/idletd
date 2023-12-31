package io.github.stealingdapenta.idletd.custommob;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class MobWrapperBuilder {
    EntityType entityType;
    Location location;
    TextComponent name;
    double speed;
    double armor;
    double attackDamage;
    double attackKnockback;
    double armorToughness;
    double knockbackResistance;
    double maxHealth;
    String playerUUID;

    public MobWrapperBuilder entityType(EntityType entityType) {
        this.entityType = entityType;
        return this;
    }

    public MobWrapperBuilder location(Location location) {
        this.location = location;
        return this;
    }

    public MobWrapperBuilder name(TextComponent name) {
        this.name = name;
        return this;
    }

    public MobWrapperBuilder speed(double speed) {
        this.speed = speed;
        return this;
    }

    public MobWrapperBuilder armor(double armor) {
        this.armor = armor;
        return this;
    }

    public MobWrapperBuilder attackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
        return this;
    }

    public MobWrapperBuilder attackKnockback(double attackKnockback) {
        this.attackKnockback = attackKnockback;
        return this;
    }

    public MobWrapperBuilder armorToughness(double armorToughness) {
        this.armorToughness = armorToughness;
        return this;
    }

    public MobWrapperBuilder knockbackResistance(double knockbackResistance) {
        this.knockbackResistance = knockbackResistance;
        return this;
    }

    public MobWrapperBuilder maxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
        return this;
    }

    public MobWrapperBuilder playerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
        return this;
    }

    public MobWrapper build() {
        return new MobWrapper(this);
    }
}
