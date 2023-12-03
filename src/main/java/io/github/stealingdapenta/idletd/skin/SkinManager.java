package io.github.stealingdapenta.idletd.skin;

import io.github.stealingdapenta.idletd.service.utils.Coloring;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

// Code based on original Design from XTen's UltraPrison server
@RequiredArgsConstructor
public class SkinManager implements Listener {
    private final Coloring coloring;
    private final SkinService skinService;

//    public Inventory createInventory() {
//        // todo untested
//        Inventory inventory = Bukkit.getServer().createInventory(null, 54, coloring.formatString(("&4&lSkins")));
//        for (int i = 0; i < getSkins().size(); i++) {
//            inventory.setItem(i, getSkins().get(i).getItem(true));
//        }
//        inventory.setItem(48, returnToMenuItem());
//        inventory.setItem(50, allSkinsItem());
//        return fillEmptySpotsWithGlassPanes(inventory);
//    }
//
//    private Inventory fillEmptySpotsWithGlassPanes(Inventory inventory) {
//        // todo untested
//        for (int i = 45; i < inventory.getStorageContents().length; i++) {
//            ItemStack item = inventory.getItem(i);
//            if (Objects.isNull(item) || item.getType() == Material.AIR)
//                inventory.setItem(i, new ItemBuilder(new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1))
//                        .setDisplayName(coloring.formatString(" "))
//                        .create());
//        }
//
//        return inventory;
//    }
//
//    private Inventory createAllInventory() {
//        // todo untested
//        Inventory inventory = Bukkit.getServer().createInventory(null, 54, coloring.formatString("&4&lAll Skins"));
//        for (Map.Entry<Integer, Skin> entry : skins.entrySet()) {
//            ItemStack item = entry.getValue().getItem(true);
//            ItemMeta meta = item.getItemMeta();
//            List<Component> lore = meta.lore();
//            lore.add(coloring.formatString(" "));
////            lore.add(coloring.formatString((true ? "&aUnlocked!" : "&cLocked")));
//            meta.lore(lore);
//            item.setItemMeta(meta);
//            inventory.addItem(item);
//        }
//        inventory.setItem(49, returnToMenuItem());
//        return fillEmptySpotsWithGlassPanes(inventory);
//    }

    private ItemStack allSkinsItem() {
        // todo untested
        ItemStack itemStack = new ItemStack(Material.EMERALD, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(coloring.formatString("&bAll Skins"));
        meta.lore(Arrays.asList(
                coloring.formatString("&eClick to view all obtainable"),
                coloring.formatString("&eskins!")
                               ));
        meta.addEnchant(Enchantment.LURE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private ItemStack returnToMenuItem() {
        // todo untested
        ItemStack itemStack = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(coloring.formatString("&aBack"));
        meta.lore(List.of(coloring.formatString("&7Return to Auto Miner menu!")));
        meta.addEnchant(Enchantment.LURE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @EventHandler // todo move to listener class?
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getWhoClicked().getOpenInventory().getTopInventory();
        //        if(!inventory.getTitle().equals(coloring.formatString("&4&lAuto Miner Skins"))) return;
        //        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        int slot = event.getRawSlot();
        // todo logic handling updating a skin
    }
//
//    @EventHandler
//    public void onPlayerInteract(PlayerInteractEvent event) {
//        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) return;
//        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
//        if (!item.hasItemMeta() || item.getType() != Material.PLAYER_HEAD) return;
//        for (Map.Entry<Integer, SkinProfile> entry : skins.entrySet()) {
//            if (entry.getValue().getItem(false).isSimilar(item)) {
//                event.setCancelled(true);
//
//                Player player = event.getPlayer();
//                // todo logic handling unlocking a skin
//                return;
//            }
//        }
//    }
}
