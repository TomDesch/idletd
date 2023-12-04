package io.github.stealingdapenta.idletd.plot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.UUID;

import static io.github.stealingdapenta.idletd.service.utils.World.TOWER_DEFENSE_WORLD;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Plot {
    // todo create a table for these static values & cache them
    private static final Vector RELATIVE_TOWER_COORDINATES = new Vector(200, 80, 50);
    private static final Vector RELATIVE_PLAYER_SPAWN_COORDINATES = new Vector(200, 90, 43);
    private static final Vector RELATIVE_MOB_SPAWN_COORDINATES = new Vector(200, 81, -13);
    private static final Vector RELATIVE_MAIN_AGENT_COORDINATES = new Vector(200.5, 81, 35.5);

    private long id;
    private int startX;
    private int startZ;
    private UUID playerUUID;

    public String getPlayerUUID() {
        return playerUUID.toString();
    }

    public Location getMobSpawnLocation() {
        return calculateLocation(RELATIVE_MOB_SPAWN_COORDINATES);
    }

    public Location calculateTowerLocation() {
        return calculateLocation(RELATIVE_TOWER_COORDINATES);
    }

    public Location getPlayerSpawnPoint() {
        Location location = calculateLocation(RELATIVE_PLAYER_SPAWN_COORDINATES);
        location.setYaw(180); // north
        location.setPitch(0);
        return location;
    }

    public Location getMainAgentLocation() {
        Location location = calculateLocation(RELATIVE_MAIN_AGENT_COORDINATES);
        location.setYaw(180); // north
        location.setPitch(0);
        return location;
    }

    private Location calculateLocation(Vector offset) {
        double x = getStartX() + offset.getX();
        double y = offset.getY();  // Y offset is absolute
        double z = getStartZ() + offset.getZ();
        return new Location(TOWER_DEFENSE_WORLD.getBukkitWorld(), x, y, z);
    }
}