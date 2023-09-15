package io.github.stealingdapenta.idletd.service.customitem;

import io.github.stealingdapenta.idletd.Idletd;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;


@RequiredArgsConstructor
public class InventoryHandler {

    public void addToInvOrDrop(Player player, ItemStack itemStack) {
        if (isInventoryFull(player)) {
            dropExcessiveItems(player, itemStack);
        } else {
            player.getInventory().addItem(itemStack);
        }
    }


    public boolean isInventoryFull(Player player) {
        return player.getInventory().firstEmpty() == -1;
    }

    public void dropExcessiveItems(Player player, ItemStack itemStack) {
        Location location = player.getLocation();

        if (Objects.isNull(location.getWorld())) return; // todo error handling

        location.getWorld().dropItemNaturally(location, itemStack);
    }

    public boolean slotContains(Inventory inventory, int slot, ItemStack itemStack) {
        ItemStack itemInSlot = inventory.getItem(slot);
        return Objects.nonNull(itemInSlot) && itemInSlot.equals(itemStack);
    }

    public boolean slotContainsSimilar(Inventory inventory, int slot, ItemStack itemStack) {
        ItemStack itemInSlot = inventory.getItem(slot);
        return Objects.nonNull(itemInSlot) && itemInSlot.isSimilar(itemStack);
    }

    public void openGUI(Player player, Inventory inventory) {
        Bukkit.getScheduler().runTaskLater(Idletd.getInstance(), () -> player.openInventory(inventory), 1L);
    }

    public boolean slotIsEmpty(Inventory inventory, int slot) {
        return Objects.isNull(inventory.getItem(slot));
    }
}
