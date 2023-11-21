package io.github.stealingdapenta.idletd.listener;

import io.github.stealingdapenta.idletd.service.customitem.TrackerItem;
import io.github.stealingdapenta.idletd.service.custommob.CustomMobHandler;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

@RequiredArgsConstructor
public class TrackerListener implements Listener {

    private final TrackerItem trackerItem;
    private final CustomMobHandler customMobHandler;

    @EventHandler
    public void onRightClickEvent(PlayerInteractEntityEvent event) {
        if (this.notMainHand(event.getHand())) {
            return;
        }

        Entity clickedEntity = event.getRightClicked();

        if (!(clickedEntity instanceof LivingEntity livingClickedEntity)) {
            return;
        }

        if (this.trackerItem.isHoldingTrackerItemInMainHand(event.getPlayer())) {
            event.setCancelled(true);

            event.getPlayer().sendMessage(
                    Component.text("You're redirecting all custom monsters towards ", TextColor.color(52, 75, 123))
                             .append(Component.text(clickedEntity.getName()))
                             .append(Component.text(". How cruel.", TextColor.color(52, 75, 123))));

            this.customMobHandler.getUpdatedLivingCustomMobs().forEach(mob -> this.customMobHandler.setNewTarget(mob, livingClickedEntity));
        }
    }

    private boolean notMainHand(EquipmentSlot slot) {
        return slot != EquipmentSlot.HAND;
    }
}
