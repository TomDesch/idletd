package io.github.stealingdapenta.idletd.service.custommob.mobtypes;

import io.github.stealingdapenta.idletd.plot.Plot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.EntityType;

public class SkeletonMob extends CustomMob {

    public SkeletonMob(Plot plot) {
        super();
        this.plot = plot;

        this.entityType = EntityType.SKELETON;

        this.ARMOR = 2.0;
        this.ATTACK_DAMAGE = 3.0;

        this.MAX_HEALTH = 14;

        this.name = Component.text("Skeleton [Lv. 100]", TextColor.color(0, 0, 120)).toBuilder().build();
    }

}