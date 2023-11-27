package io.github.stealingdapenta.idletd.skin;

import io.github.stealingdapenta.idletd.service.customitem.ItemBuilder;
import io.github.stealingdapenta.idletd.service.utils.Coloring;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// Code based on original Design from XTen's UltraPrison server
public class SkinManager implements Listener {

    private static final String NONE = "&7None";

    private final Map<Integer, SkinProfile> skins = new LinkedHashMap<>();
    private final Coloring coloring;

    public SkinManager() {
        this.coloring = new Coloring();
        registerSkins();
    }

    private void registerSkins() {
        skins.put(1, new SkinBuilder()
                .of("eyJ0aW1lc3RhbXAiOjE1MzI5OTc4NTQyMjMsInByb2ZpbGVJZCI6IjRlOTRmNDVjNzU5YzQyOTNhZjE2ZmRmMjE5MjM3YTVkIiwicHJvZmlsZU5hbWUiOiJNaW5lciIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQ4ODcwY2E3N2FhNjk5ZDAxNzA5M2I4Mjc3OWQwYTU5OGRjNGYzZjQ5ZDM1MTUzMmFlNGQ5NjMzZGNjMmE1YSJ9fX0=", "BanXVBeGVWDILi3jhtDoVXGIjEwtX5MD15jR5DqIfEF65GIz/bYlVttktNmivug2GOhXxw0E9JLT4Hb4I/uz2vN7TWwM4gm210hFcLNtaJQEXSw0s0rikSAztE0tXWhukZZ82c4LJVDwujGHgGrrgWlE50ogZILB4wDOmglT+wRAWKgaBwBhiY+qckNxgGO4hVy4XfU3keetqwj63j8wuVg0Hef2aTF9QMSqzhURbbbjMA1XisujT3rxCXrpo6jQpCKFbP0LaM+DtWMCPBdBsTBbSXJj48wdSf0lBXL31EkcdBvTAKcpXNfn20YxClgnCj0Uat+R7HV3uGkDuBEDDF7uhMo+aStunBWoDrkRlhcnio+CmHUp3aakcamPulZN9HIa0Ru/ngaxsRRFRvDcP7/czXNf7rFd9YSlClWPDihyDnPwjiPq4TtiZuhitdwD1jK5uWrw51wBEFpUpZLz5FEtxXNLUtTi5CnYtjTS5PLJVD8tFwOBFGCRMbCBSzHSQZv6Ox41Q5rIfzMwZoJZe2wfjMcxLr3o3/IYRD3qcgEwG58HNllo5lS2fPpOSdSs2NUeUWhaZmTXRI3s2htsUcPmx8O7df+D8CMWO4qgMYjFCV/ka29lclkX34RpaobC5yfJuon/46B3VOmJh9idDO1ZJUHtT3olD0XrCOQNlrc=")
                .withItemName("&eDefault")
                .addDesc("Default skin")
                .addBonus(NONE)
                .withObtainable(false)
                .build());
        skins.put(2, new SkinBuilder()
                .of("eyJ0aW1lc3RhbXAiOjE1MTgyNTQ4MjQ0OTQsInByb2ZpbGVJZCI6ImIwZDczMmZlMDBmNzQwN2U5ZTdmNzQ2MzAxY2Q5OGNhIiwicHJvZmlsZU5hbWUiOiJPUHBscyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDJiNzYzMTc3YTdkN2UzN2E1YmNkMTkwMzAzYzI0MjQyMWY1Njg3YmRkZGIyMTc0N2EzZjA5MTMzZDUzNjg1In19fQ==", "JRLfdFqhjxEGuFnkfhgyLw7/kGqmJS9a5p7oJDMqG9zeaV4E0lfcU2UXH9j8+3gblHTYYS2lftQ3BS2AJx2vA4A+3Y2Wj0C4t6BRa87RVZIJfEObbDFnCuESwwQYpJ+vyefI224Uto7cOJfqcq9G/TeV/tkZkfuPjJs1xw+ZX4gsVYPzFqhdlejRrmAfZJ5KasRLSi2HVCk7y/Vh6a0mPZm12eRK15RaANoQKqkA8dpSvW/OF/coh9ig0ydGUmCBjGphg5LkgFSQv+iFIeDJTz6CNbZrnb1haDq1HxKWjkJWDKmuBzMug04vkue5bxYNTVKRPazEh0PSdv2RmNLsClRMhFepN1vQUarJTRwEwHLR6mdjPsbb/LYvdVzqlgyO2y/XBcdWbOUSq0486S9vzHHDMvb0hCsKHWkKEyrs5O73+QtJhSU4iKGGJHwf3I3VC9wJlBAFNkZT7IU3B+ZBNJlG3IGqfB3Rda75dZDT5a9NKrwrdgi58lsNkZziu5tUOSFLIsB7aWJnDoyAXAe+AQopOmv+oKvP1EbdKBGAGOY6s4/UgnIytTCiSQSBb63QBDLfB6AoVXCfa48mruAXvBLUGt1BYQ7tXWA8vWdi01TMPx0iCfISW8FQJ/1pb4LHxRPrVJEFGSqt6kH7X+UNj+oX16mehTgSMKi1GPOOvu0=")
                .withItemName("&eGold Skin")
                .addDesc("Free skin for &7members")
                .addBonus(NONE)
                .withObtainable(false)
                .build());
        skins.put(3, new SkinBuilder()
                .of("eyJ0aW1lc3RhbXAiOjE1MDQ5OTc3NzQ3NzAsInByb2ZpbGVJZCI6" +
                            "IjhiNTcwNzhiZjFiZDQ1ZGY4M2M0ZDg4ZDE2NzY4ZmJlIiwicHJvZmlsZU5hbWUiOiJNSEZfUGlnIiwic" +
                            "2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1p" +
                            "bmVjcmFmdC5uZXQvdGV4dHVyZS82MjE2NjhlZjdjYjc5ZGQ5YzIyY2UzZDFmM2Y0Y2I2ZTI1NTk4OTNiNmRmNGE" +
                            "0Njk1MTRlNjY3YzE2YWE0In19fQ==",
                    "bxFlQVB1ClB+Sb1w6fFtAD5dXx3Jj7+mKq8Fd5rpUhrRWJqxfqqVZkFCcTMrlXW6YkpQUTdi/TuHj9KSWam21oFJt0A0Niln+1TIFfGKLaXrwSzJHiq+evDuXiAfOjw/5OcpuQKRnLkMG3zOyyWb/dNAuHnvpYMl1MiscFEF6+Me" +
                            "+/KBChWnvjLY1MjxYPk4RU1xadR1xD631lHZioN4Vqoqjec0+xDkSekmJpkkLy+x06jDfLvu5Bg4CcuSPbEcjt37vGIynrePYPrtW3qpFrJsbOCJtx7azUrFWZkbi3tqXkHS8f9tkCucYQhFHfBFH4kqb122V7" +
                            "/9EchqZC2zWEtMBBVsXKJMUwhUit3XV0YGd2KwRVohjh9c4jA+N+xxMJ/fSN4Al35W9SsyNE0uOZUKGyMkhofIIidXDsZgttEymrjWC64M1ei32SlH1PoAwSj9cXD3TJ8y4r3A9NCxjhOot8g2EfykSxy" +
                            "/nFRBrHedQ93rMhIooGkWQktM+pbY3rgc70/HeNpWmqvr8rSBFUkKVPKII33mo5Y1nj+yAG4MHKVpCs+DBTmAbfA/ojET9E7UyC4K3uR6BYT0sGVrZAVbjud6zDKnWl1kpe5s9nFV5YBvKZKyf9nIokZiYgw" +
                            "/VQkPtfENzLuEBkYgTc2b0mckUDiboz4AT+05c1GkZWI=")
                .withItemName("&dPig Skin")
                .addDesc("Rare drop")
                .addBonus(NONE)
                .withObtainable(false)
                .build());
        skins.put(4, new SkinBuilder()
                .of("eyJ0aW1lc3RhbXAiOjE1NDAxNDIxNzcxNDUsInByb2ZpbGVJZCI6ImRmYWFkNTUxNGU3ZTQ1YTFhNmY3YzZmYzVlYzgyM2FjIiwicHJvZmlsZU5hbWUiOiJNSEZfU2hlZXAiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzdjYTM4Y2NmNDE3ZTk5Y2E5ZDQ3ZWViMTVhOGEzMGVkYjE1MDdhYTUyYjY3OGMyMjBjNzE3YzQ3NGFhNmZlM2UifX19", "BkT6wzgguXEAqq/k1oflfz3EBA6ynFDdSO/uXv7JGc7VtO6AYpAJOjVI5zzUi6IdApHjMvL0W81wPDNUT4WGs6iFOZc5jeluDJnCKyfaTec3vUwv80t+iN/fSwMgvUeTKq+IdroXCxqMEo+snK1Yco+hcUs+VqafEadrZr7cQpOnBQ9E2kkQEhR1yLZna24icTM0Ruk9hlMW/0ZisqFkqhif8Gr70rzTeVk9j+qfUbFPJMsZyerv2MIYhG0qLaKsWiRv0xXUvIGnqeRrNFakDGy9adwbL+Xc7NJ11iPkmR02SmuLvgfM6v/q69n+7f9YvQFTJItbjY3V5AJWTbGQp7IaRu62wY1jNrMoCzjV3IjrAN/ruCiFXd+zIAmorK75NWss6g+NlT1QHHZkXZOhDA60g6op6WiAQ9YrTXETF8fG63lE2rzwOcW5KYuydXHD5qKbxF/TGFK69XOmWFyvgdBEbi7C5RP4IFdu2bZVpV34+tb6LawtWfbDZ0xhZorR9MhAJK3lJFn3MAzkKpbaJxh79Q3EsR6Nm5MMk3fj/uDh978rO9627BD2e8ulXRber5dGuzm+k2xcGuou2Q4ZUTeiPKVSON8ttOPt5Iql14J9s5ZQ7eeUlEELSJIzGw4wGPwqh1gWQjP9T7eiwGhyUIL8RYKCoBnGu2my1U4zWGA=")
                .withItemName("&fSheep Skin")
                .addDesc("Rare drop from the Arena")
                .addBonus(NONE)

                .withObtainable(false)
                .build());
        skins.put(5, new SkinBuilder()
                .of("eyJ0aW1lc3RhbXAiOjE1MDI1NDE4ODgyNDgsInByb2ZpbGVJZCI6ImJiMDgwOTFhOTJlZDQzYWQ5NGYxZWRmZDc0NmIwYTJjIiwicHJvZmlsZU5hbWUiOiJJcDRuZHgiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzVkN2ZkODg5MzdjNWZkZWI5MmE2ODM4Y2YzY2ViMjdjNGUzOTVjY2NkMmRjYWRkMWMyYjZlMjg0ZmRkYjRhIn19fQ==", "lKhDCCVd4QX/50hDJVSQnnDHWQfQFE5MICA8WyJau++pNPbByqoMcEoIa9OkAnrjAVBiQ/2SciZlqYWok8nMVP4IihwcRRdr5duRcNbMzQZKT4mU0joC7RvWYpvlE1z57/iNWsVZzVMOoBopmxN5V8C1ZubiJE/28jdjXGtS4+pTgzZPE8f7AeEqok0UNGjhN2KQLS6UO4nxhA/ZmH47W72DLMgg4mDQkUUvNvUBbl5GdC6W7woPazhfsb5Sg86ff/Xg0p48HhzMegKY9yIe7P/3Fu9WFVooRrS9eDWHJ0rIEim0bc4c+rRJ6JuwDl62qtT9TEf1k/PN4DWLyQ5INi3k2t2ldekF6Rdla4qtimn0quzB8zuA1pIupg1mUk3jhvM+CS+/TnpZatesJ6MDQ3Zyn3fMMRaFP/bppbWywPFFiNkwjdTN9bwKLLIs4SXWNEH5Fv6we2//D3N7G1s1M/PIGLDk9kpBKJDw5rC3/hSK13JFUjnfNGk2VrcIDtXLvAzecyXRqtvN8Bj8XvYbBV/lN3Pnxv7b0rbyzn2FIRNRVt7fo5x2KAR1GXgwUAMVFeSg7uqL9t3sXjxMAwFHh1bOjoTZyrNxwjYOCsyTgLkGPHHVAFsWGmqCxHEPM+dQUxzfTY8tmPoXjvcUoKJyBePbKsFIQfHaFjXKzv2tjJ4=")
                .withItemName("&bGuardian Skin")
                .addDesc("Rare drop")
                .addBonus(NONE)
                .withObtainable(false)
                .build());
    }

    public Map<Integer, SkinProfile> getSkins() {
        return skins;
    }

    public Inventory createInventory() {
        // todo untested
        Inventory inventory = Bukkit.getServer().createInventory(null, 54, coloring.formatString(("&4&lSkins")));
        for (int i = 0; i < getSkins().size(); i++) {
            inventory.setItem(i, getSkins().get(i).getItem(true));
        }
        inventory.setItem(48, returnToMenuItem());
        inventory.setItem(50, allSkinsItem());
        return fillEmptySpotsWithGlassPanes(inventory);
    }

    private Inventory fillEmptySpotsWithGlassPanes(Inventory inventory) {
        // todo untested
        for (int i = 45; i < inventory.getStorageContents().length; i++) {
            ItemStack item = inventory.getItem(i);
            if (Objects.isNull(item) || item.getType() == Material.AIR)
                inventory.setItem(i, new ItemBuilder(new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1))
                        .setDisplayName(coloring.formatString(" "))
                        .create());
        }

        return inventory;
    }

    private Inventory createAllInventory() {
        // todo untested
        Inventory inventory = Bukkit.getServer().createInventory(null, 54, coloring.formatString("&4&lAll Skins"));
        for (Map.Entry<Integer, SkinProfile> entry : skins.entrySet()) {
            ItemStack item = entry.getValue().getItem(true);
            ItemMeta meta = item.getItemMeta();
            List<Component> lore = meta.lore();
            lore.add(coloring.formatString(" "));
//            lore.add(coloring.formatString((true ? "&aUnlocked!" : "&cLocked")));
            meta.lore(lore);
            item.setItemMeta(meta);
            inventory.addItem(item);
        }
        inventory.setItem(49, returnToMenuItem());
        return fillEmptySpotsWithGlassPanes(inventory);
    }

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

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (!item.hasItemMeta() || item.getType() != Material.PLAYER_HEAD) return;
        for (Map.Entry<Integer, SkinProfile> entry : skins.entrySet()) {
            if (entry.getValue().getItem(false).isSimilar(item)) {
                event.setCancelled(true);

                Player player = event.getPlayer();
                // todo logic handling unlocking a skin
                return;
            }
        }
    }
}
