package io.github.stealingdapenta.idletd.service.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import org.bukkit.Bukkit;

import java.util.Objects;

public enum World {
    TOWER_DEFENSE_WORLD("idletd");


    private final String worldName;

    World(String worldName) {
        this.worldName = worldName;
    }

    public boolean equals(org.bukkit.World bukkitWorld) {
        return Objects.equals(Bukkit.getWorld(this.worldName), bukkitWorld);
    }

    public org.bukkit.World getBukkitWorld() {
        return Bukkit.getWorld(this.worldName);
    }

    public com.sk89q.worldedit.world.World getSk89qWorld() {
        return BukkitAdapter.adapt(this.getBukkitWorld());
    }
}
