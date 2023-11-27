package io.github.stealingdapenta.idletd.skin;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.github.stealingdapenta.idletd.service.utils.Coloring;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

// Code based on original Design from XTen's UltraPrison server
public class SkinProfile {

    private final String data;
    private final String signature;
    private final boolean obtainable;
    private final String itemName;
    private final List<String> description;
    private final List<String> bonuses;
//    private BiConsumer<PrisonPlayer, AutoMinerReward> consumer;

    private final Coloring coloring;

    public SkinProfile(String data, String signature, boolean obtainable, String itemName, List<String> description, List<String> bonuses) {
        this.data = data;
        this.signature = signature;
        this.obtainable = obtainable;
        this.itemName = itemName;
        this.description = description;
        this.bonuses = bonuses;
//        this.consumer = consumer;
        this.coloring = new Coloring();
    }


    public String getData() {
        return data;
    }

    public String getSignature() {
        return signature;
    }

    public boolean isObtainable() {
        return obtainable;
    }

//    public void apply(PrisonPlayer prisonPlayer, AutoMinerReward autoMinerReward){
//        consumer.accept(prisonPlayer, autoMinerReward);
//    }

    public ItemStack getItem(boolean inventoryIcon) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        Set<ProfileProperty> properties = profile.getProperties();
        properties.add(new ProfileProperty("textures", data));
        itemMeta.setPlayerProfile(profile);

        itemMeta.displayName(coloring.formatString(itemName));

        List<TextComponent> lore = description.stream()
                                              .map(string -> "&7" + string)
                                              .map(coloring::formatString)
                                              .collect(Collectors.toList());

        if (!bonuses.isEmpty()) {
            lore.add(Component.text(" ").toBuilder().build());
            lore.add(coloring.formatString("&6Bonuses: "));
            bonuses.forEach(string -> lore.add(coloring.formatString("&7- &a" + string)));
        }

        lore.add(Component.text(" ").toBuilder().build());

        if (inventoryIcon) {
            lore.add(coloring.formatString("&b» Click to equip!"));
        } else {
            lore.add(coloring.formatString("&b» Right click to redeem!"));
        }

        itemMeta.lore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

}
