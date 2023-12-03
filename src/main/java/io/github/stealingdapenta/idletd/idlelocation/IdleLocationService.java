package io.github.stealingdapenta.idletd.idlelocation;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;

@RequiredArgsConstructor
public class IdleLocationService {
    private final IdleLocationRepository idleLocationRepository;

    public Location getLocation(int id) {
        IdleLocation idleLocation = idleLocationRepository.getIdleLocation(id);
        if (Objects.nonNull(idleLocation)) {
            return convert(idleLocation);
        }
        return null;
    }

    public int save(Location location) {
        return save(convert(location));
    }

    public int save(IdleLocation idleLocation) {
        return idleLocationRepository.saveIdleLocation(idleLocation);
    }

    public void update(Location location) {
        update(convert(location));
    }

    public void update(IdleLocation idleLocation) {
        idleLocationRepository.updateIdleLocation(idleLocation);
    }

    public IdleLocation convert(Location location) {
        return IdleLocation.builder()
                           .worldName(location.getWorld().getName())
                           .x(location.getX())
                           .y(location.getY())
                           .z(location.getZ())
                           .pitch(location.getPitch())
                           .yaw(location.getYaw())
                           .build();
    }

    public Location convert(IdleLocation idleLocation) {
        return new Location(Bukkit.getWorld(idleLocation.getWorldName()), idleLocation.getX(), idleLocation.getY(), idleLocation.getZ(), idleLocation.getYaw(), idleLocation.getPitch());
    }
}
