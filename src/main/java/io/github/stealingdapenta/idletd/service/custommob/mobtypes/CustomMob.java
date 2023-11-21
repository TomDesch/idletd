package io.github.stealingdapenta.idletd.service.custommob.mobtypes;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.plot.Plot;
import io.github.stealingdapenta.idletd.service.custommob.CustomMobHandler;
import io.github.stealingdapenta.idletd.service.custommob.MobWrapperBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

@RequiredArgsConstructor
@Setter
@Getter
public abstract class CustomMob {

    protected EntityType entityType;
    protected Mob mob;
    protected Plot plot;

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

        mob = (Mob) new CustomMobHandler().spawnCustomMob(customMob).getSummonedEntity();

        preventMobFromFallingTask();

        return mob;
    }

    private BukkitTask preventMobFromFallingTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (mob.isDead() || Objects.isNull(plot)) {
                    cancel();
                }
                if (mob.getLocation().getY() < plot.getMobSpawnLocation().getY() - 5) {
                    // Teleport the mob back to the spawn location
                    mob.teleport(plot.getMobSpawnLocation());
                }
            }
        }.runTaskTimer(Idletd.getInstance(), 0L, 30L);  // 20 ticks = 1 second
    }
}
