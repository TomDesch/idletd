package io.github.stealingdapenta.idletd.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import static io.github.stealingdapenta.idletd.service.utils.World.TOWER_DEFENSE_WORLD;

@RequiredArgsConstructor
public class SpawnListener implements Listener {

    @EventHandler
    public void preventNaturalSpawningInTowerWorld(CreatureSpawnEvent event) {
        if (!TOWER_DEFENSE_WORLD.equals(event.getLocation().getWorld())) {
            return;
        }

        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {
            event.setCancelled(true);
        }
    }
}