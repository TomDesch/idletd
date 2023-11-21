package io.github.stealingdapenta.idletd.service.custommob;

import io.github.stealingdapenta.idletd.plot.Plot;
import io.github.stealingdapenta.idletd.service.custommob.mobtypes.ZombieMob;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@RequiredArgsConstructor
public class CustomMobSpawner {


    public void spawnInFrontOfPlayer(Player player, int distance) {
        Location loc = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize().multiply(distance);
        Location front = loc.add(direction);

        ZombieMob zombieMob = new ZombieMob(new Plot((int) player.getLocation().getX(), (int) player.getLocation().getZ(), player.getUniqueId()));
        zombieMob.summon(front);
    }
}
