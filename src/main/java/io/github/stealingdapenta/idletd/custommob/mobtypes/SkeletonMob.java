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
}