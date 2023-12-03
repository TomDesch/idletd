package io.github.stealingdapenta.idletd.skin;


import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.github.stealingdapenta.idletd.service.utils.Coloring;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class SkinService {
    private final SkinRepository skinRepository;
    private final Coloring coloring;

    public Skin getSkin(long id) {
        return skinRepository.getSkin(id);
    }

    public List<Skin> getAllSkins() {
        return skinRepository.getAllSkins();
    }

    public ItemStack getItem(Skin skin) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        Set<ProfileProperty> properties = profile.getProperties();
        properties.add(new ProfileProperty("textures", skin.getDataToken()));
        itemMeta.setPlayerProfile(profile);

        itemMeta.displayName(coloring.formatString(skin.getName()));

        List<TextComponent> lore = new ArrayList<>();
        lore.add(Component.text(skin.getDescription()).toBuilder().build());
        lore.add(Component.text(" ").toBuilder().build());

        if (true) { // GUI item or redeemable item?
            lore.add(coloring.formatString("&b» Click to equip!"));
        } else {
            lore.add(coloring.formatString("&b» Right click to redeem!"));
        }

        itemMeta.lore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

}
