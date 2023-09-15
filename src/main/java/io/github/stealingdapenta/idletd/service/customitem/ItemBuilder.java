package io.github.stealingdapenta.idletd.service.customitem;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ItemBuilder {

    private final ItemStack stack;
    private ItemMeta meta;
    private final List<Component> loreList = new ArrayList<>();

    public ItemBuilder(ItemStack stack){
        this.stack = stack.clone();
        this.meta = stack.getItemMeta();
    }

    public ItemBuilder(Material material){
        this(new ItemStack(material, 1));
    }

    public ItemBuilder setDisplayName(Component name){
        meta = meta == null ? stack.getItemMeta() : meta;
        meta.displayName(name);
        return this;
    }

    public ItemBuilder modifyMeta(Consumer<ItemMeta> consumer){
        consumer.accept(meta);
        return this;
    }

    public ItemBuilder addLore(Component... lore){
        meta = meta == null ? stack.getItemMeta() : meta;
        List<Component> temp = Arrays.asList(lore);

        loreList.addAll(temp);
        meta.lore(loreList);

        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... itemFlags){
        meta = meta == null ? stack.getItemMeta() : meta;
        meta.addItemFlags(itemFlags);

        return this;
    }

    public ItemBuilder setEnchantments(Map<Enchantment, Integer> enchants){
        enchants.forEach(stack::addUnsafeEnchantment);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, Integer level){
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder setGlowing(boolean glow){
        if(!glow) return this;
        meta = meta == null ? stack.getItemMeta() : meta;
        meta.addEnchant(Enchantment.LURE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder setGlowing(){
        return this.setGlowing(true);
    }

    public ItemStack create(){
        meta = meta == null ? stack.getItemMeta() : meta;
        stack.setItemMeta(meta);
        return stack;
    }
}
