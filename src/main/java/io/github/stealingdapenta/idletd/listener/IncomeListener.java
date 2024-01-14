package io.github.stealingdapenta.idletd.listener;

import static io.github.stealingdapenta.idletd.Idletd.logger;

import io.github.stealingdapenta.idletd.custommob.CustomMobHandler;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerManager;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.idleplayer.stats.BalanceHandler;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

@RequiredArgsConstructor
public class IncomeListener implements Listener {

    private final CustomMobHandler customMobHandler = CustomMobHandler.getInstance();
    private final IdlePlayerService idlePlayerService;
    private final IdlePlayerManager idlePlayerManager;
    private final BalanceHandler balanceHandler;


    @EventHandler
    public void checkDeadCustomMobs(EntityDeathEvent event) {
        // POC lmao this is botched AF todo
        // make it so pay is calculated
        // and dont give pay if killed by someone else or by log out despawn
        // also todo fix the bug where relog relog dring timer doesnt stop the mobs from spawning
        // todo bug lmao idlePlayer is always null
        LivingEntity livingEntity = event.getEntity();
        if (!customMobHandler.isCustomMob(livingEntity)) {
            return;
        }

        String linkedPlayerUUID = livingEntity.getPersistentDataContainer()
                                              .get(customMobHandler.getPlayerNameSpacedKey(), PersistentDataType.STRING);
        if (Objects.isNull(linkedPlayerUUID)) {
            logger.warning("Found a custom mob, but without a linked player UUID!");
            return;
        }

        Player linkedPlayer = idlePlayerService.getPlayer(linkedPlayerUUID);
        IdlePlayer savedIdlePlayer = idlePlayerService.getIdlePlayer(linkedPlayer);

        if (!idlePlayerManager.isOnline(savedIdlePlayer)) {
            logger.info("Player isn't online: " + linkedPlayer.getName());
        }

        IdlePlayer idlePlayer = idlePlayerManager.getOnlineIdlePlayer(linkedPlayer);
        if (Objects.isNull(idlePlayer)) {
            logger.info("Weirdly enough, the linked idle player that will receive this loot is not online: " + linkedPlayer.getName());
            idlePlayer = idlePlayerService.getIdlePlayer(linkedPlayer);
        }

        balanceHandler.pay(idlePlayer, 10D);
    }
}

