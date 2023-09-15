package io.github.stealingdapenta.idletd.service.customitem;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class TrackerItem {

    public ItemStack getTrackerItem() {

        Component itemName = Component.text()
                .append(Component.text("Tracker", NamedTextColor.GOLD)
                        .append(Component.text(" item", NamedTextColor.AQUA)))
                .build();

        Component itemLore = Component.text()
                .append(Component.text("Right click a mob to make all custom monsters track it.", TextColor.color(123, 62, 38)))
                .build();

        return new ItemBuilder(Material.STICK)
                .setDisplayName(itemName)
                .addLore(itemLore)
                .setGlowing()
                .create();
    }

    public boolean isHoldingTrackerItemInMainHand(Player player) {
        return player.getInventory().getItemInMainHand().isSimilar(this.getTrackerItem());
    }
}
