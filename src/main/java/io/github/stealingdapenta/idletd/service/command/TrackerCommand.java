package io.github.stealingdapenta.idletd.service.command;

import io.github.stealingdapenta.idletd.service.customitem.InventoryHandler;
import io.github.stealingdapenta.idletd.service.customitem.TrackerItem;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class TrackerCommand implements CommandExecutor {

    private final InventoryHandler inventoryHandler;
    private final TrackerItem trackerItem;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("Summoning tracker item");
        Player player = (Player) commandSender;

        ItemStack trackerItemStack = this.trackerItem.getTrackerItem();
        this.inventoryHandler.addToInvOrDrop(player, trackerItemStack);
        return true;
    }
}
