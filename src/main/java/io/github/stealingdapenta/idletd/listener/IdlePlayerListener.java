package io.github.stealingdapenta.idletd.listener;

import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerManager;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;
import java.util.UUID;

import static io.github.stealingdapenta.idletd.Idletd.isShuttingDown;
import static io.github.stealingdapenta.idletd.Idletd.logger;
import static io.github.stealingdapenta.idletd.idleplayer.IdlePlayerManager.getNoLoginAllowed;
import static io.github.stealingdapenta.idletd.idleplayer.IdlePlayerManager.getOfflinePlayerCache;

@RequiredArgsConstructor
@Setter
@Getter
public class IdlePlayerListener implements Listener {
    private static final String IDLETD_UNAVAILABLE = "&cIdle TD is currently unavailable";
    private static final String WAIT_BEFORE_LOGGING = "&cPlease wait before logging in!";
    private final IdlePlayerManager idlePlayerManager;
    private final IdlePlayerService idlePlayerService;
    private long lastNewPlayer = -1;

    @EventHandler
    public void preLogin(AsyncPlayerPreLoginEvent event) {
        if (isShuttingDown()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text(IDLETD_UNAVAILABLE));
            return;
        }
        if (getNoLoginAllowed().contains(event.getUniqueId())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text(WAIT_BEFORE_LOGGING));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (isShuttingDown()) {
            player.kick(Component.text(IDLETD_UNAVAILABLE));
            return;
        }

        IdlePlayer idlePlayer;

        IdlePlayer cachedIdlePlayer = getOfflinePlayerCache().getIfPresent(uuid);
        if (Objects.nonNull(cachedIdlePlayer)) {
            idlePlayer = cachedIdlePlayer;
            idlePlayerManager.registerOnlinePlayer(idlePlayer);
            getOfflinePlayerCache().invalidate(uuid);
        } else {
            idlePlayer = idlePlayerService.getIdlePlayer(uuid);

            if (Objects.isNull(idlePlayer)) {
                idlePlayer = idlePlayerService.createNewIdlePlayer(uuid);
            }
            // broadcast welcome msg
        }

        idlePlayerManager.postLogin(idlePlayer);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        handleLogOut(player);
        // todo quit message
        event.quitMessage(null);
    }

    private void handleLogOut(Player player) {
        try {
            IdlePlayer idlePlayer = idlePlayerService.getIdlePlayer(player);
            if (!isShuttingDown()) {
                idlePlayerManager.savePlayerData(idlePlayer);
            }
        } catch (Exception e) {
            logger.warning("Error in log out operations for " + player.getName());
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        handleLogOut(player);
        // todo quit message
        event.leaveMessage(null);
    }

    @EventHandler
    public void onPlayerKickServerFull(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.KICK_FULL) {
            event.allow();
        }
    }
}
