package io.github.stealingdapenta.idletd.service.custommob.mobtypes;

import io.github.stealingdapenta.idletd.service.custommob.CustomMobHandler;
import io.github.stealingdapenta.idletd.service.custommob.MobWrapperBuilder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

@RequiredArgsConstructor
@Setter
public abstract class CustomMob {

    protected EntityType entityType;

    protected double ARMOR = 1.0;
    protected double ATTACK_DAMAGE = 1.0;
    protected double ATTACK_KNOCKBACK = 1.0;
    protected double ARMOR_TOUGHNESS = 1.0;
    protected double ATTACK_SPEED = 1.0;
    protected double FLYING_SPEED = 1.0;
    protected double KNOCKBACK_RESISTANCE = 1.0;
    protected double MAX_HEALTH = 10.0;
    protected double MOVEMENT_SPEED = 1.0;
    protected TextComponent name = Component.text("Custom mob", TextColor.color(146, 9, 9)).toBuilder().build();


    public Mob summon(Location location) {
        MobWrapperBuilder customMob = new MobWrapperBuilder()
                .location(location)
                .name(name)
                .entityType(entityType)
                .armor(ARMOR)
                .attackDamage(ATTACK_DAMAGE)
                .attackKnockback(ATTACK_KNOCKBACK)
                .armorToughness(ARMOR_TOUGHNESS)
                .attackSpeed(ATTACK_SPEED)
                .flyingSpeed(FLYING_SPEED)
                .knockbackResistance(KNOCKBACK_RESISTANCE)
                .maxHealth(MAX_HEALTH)
                .speed(MOVEMENT_SPEED);

        return (Mob) new CustomMobHandler().spawnCustomMob(customMob);
    }
}
