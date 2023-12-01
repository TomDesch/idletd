package io.github.stealingdapenta.idletd.idleplayer;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.stealingdapenta.idletd.Idletd;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static io.github.stealingdapenta.idletd.Idletd.isShuttingDown;

@RequiredArgsConstructor
@Getter
@Setter
public class IdlePlayerManager {
    public static final List<IdlePlayer> onlinePlayers = new ArrayList<>();
    private static final Logger logger = Idletd.getInstance().getLogger();
    public static Cache<UUID, IdlePlayer> offlinePlayerCache = CacheBuilder.newBuilder().expireAfterAccess(20, TimeUnit.MINUTES).build();
    public static volatile Set<UUID> noLoginAllowed = new HashSet<>();

    private final IdlePlayerService idlePlayerService;


    public boolean registerOnlinePlayer(IdlePlayer idlePlayer) {
        if (onlinePlayers.contains(idlePlayer)) return false;
        return onlinePlayers.add(idlePlayer);
    }

    public boolean deregisterOnlinePlayer(IdlePlayer idlePlayer) {
        if (!onlinePlayers.contains(idlePlayer)) return false;
        return onlinePlayers.remove(idlePlayer);
    }

    public void postLogin(IdlePlayer idlePlayer) {
        // Generate player scoreboard
        // Generate stuff based on settings e.g. username
        // Handle lastLogin time etc
        idlePlayerService.getPlayer(idlePlayer.getPlayerUUIDAsUUID()).setGlowing(false);
    }


    public void savePlayerData(IdlePlayer idlePlayer) {
        if (Objects.isNull(idlePlayer) || noLoginAllowed.contains(idlePlayer.getPlayerUUIDAsUUID())) {
            return;
        }

        // Protect data integrity by not allowing the player to log back in during this process.
        noLoginAllowed.add(idlePlayer.getPlayerUUIDAsUUID());

        try {
            // todo saving data

        } catch (Exception e) {
            logger.warning("&eError saving user data for " + idlePlayerService.getPlayer(idlePlayer).getName());
            e.printStackTrace();
        } finally {
            if (!isShuttingDown) {
                deregisterOnlinePlayer(idlePlayer);
                noLoginAllowed.remove(idlePlayer.getPlayerUUIDAsUUID());
            }
        }
    }
}
