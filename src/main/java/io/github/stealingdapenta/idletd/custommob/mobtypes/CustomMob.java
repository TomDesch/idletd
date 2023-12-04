package io.github.stealingdapenta.idletd.custommob.mobtypes;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.custommob.CustomMobHandler;
import io.github.stealingdapenta.idletd.custommob.MobWrapperBuilder;
import io.github.stealingdapenta.idletd.plot.Plot;
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

import java.util.Objects;

import static io.github.stealingdapenta.idletd.service.utils.Time.ONE_SECOND_IN_TICKS;
import static io.github.stealingdapenta.idletd.service.utils.Time.ZERO_TICKS;

@RequiredArgsConstructor
@Setter
@Getter
public abstract class CustomMob {

    protected EntityType entityType;
    protected Mob mob;
    protected Plot plot;
    protected int level;

    protected double ARMOR = 1.0;
    protected double ATTACK_DAMAGE = 1.0;
    protected double ATTACK_KNOCKBACK = 1.0;
    protected double ARMOR_TOUGHNESS = 1.0;
    protected double KNOCKBACK_RESISTANCE = 1.0;
    protected double MAX_HEALTH = 10.0;
    protected double MOVEMENT_SPEED = 0.2;

    protected TextColor nameColor = TextColor.color(146, 9, 9);

    public Mob summon(Location location) {
        MobWrapperBuilder customMob = new MobWrapperBuilder()
                .playerUUID(plot.getPlayerUUID())
                .location(location)
                .name(generateMobName())
                .entityType(entityType)
                .armor(ARMOR)
                .attackDamage(ATTACK_DAMAGE)
                .attackKnockback(ATTACK_KNOCKBACK)
                .armorToughness(ARMOR_TOUGHNESS)
                .knockbackResistance(KNOCKBACK_RESISTANCE)
                .maxHealth(MAX_HEALTH)
                .speed(MOVEMENT_SPEED);

        mob = (Mob) new CustomMobHandler().spawnCustomMob(customMob).getSummonedEntity();

        // todo set Target to the main agent of the plot (we have this.plot)
        preventMobFromFallingTask();

        generateMobName();

        return mob;
    }

    private TextComponent generateMobName() {
        String name = getEntityType().name() + " [Lv." + getLevel() + "]";
        return Component.text(name, getNameColor()).toBuilder().build();
    }

    // todo in the future have one task for all mobs.
    private void preventMobFromFallingTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Objects.isNull(plot) || !mob.isValid()) {
                    cancel();
                }

                if (mob.getFallDistance() > 5) {
                    mob.setFallDistance(0); // prevents fall dmg
                    mob.teleport(plot.getMobSpawnLocation());
                }
            }
        }.runTaskTimer(Idletd.getInstance(), ZERO_TICKS, 2 * ONE_SECOND_IN_TICKS);
    }
}
