package io.github.stealingdapenta.idletd.service.custommob.mobtypes;

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

        // Stats
        this.ARMOR = 2.0 * 1;
        this.ATTACK_DAMAGE = 3.0;
        this.MAX_HEALTH = 12;
    }

    // Goes from light green to dark green based on Level (peaking at Lv. 1000)
    private TextColor generateNameColor() {
        int colorValue = 255 - level / 4;
        colorValue = Math.max(colorValue, 0);
        return TextColor.color(0, colorValue, 0);
    }

}
