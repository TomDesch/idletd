package io.github.stealingdapenta.idletd.listener;

import io.github.stealingdapenta.idletd.service.custommob.CustomMobHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

@RequiredArgsConstructor
public class CustomMobListener implements Listener {
    private final CustomMobHandler customMobHandler;


    @EventHandler
    public void checkDeadCustomMobs(EntityDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();

        if (customMobHandler.isCustomMob(livingEntity)) {
            customMobHandler.removeDeadMobsFromList();
        }
    }
}



