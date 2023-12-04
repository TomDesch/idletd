package io.github.stealingdapenta.idletd.idleplayer;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.stealingdapenta.idletd.agent.AgentManager;
import io.github.stealingdapenta.idletd.towerdefense.TowerDefense;
import io.github.stealingdapenta.idletd.towerdefense.TowerDefenseManager;
import io.github.stealingdapenta.idletd.towerdefense.TowerDefenseService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.github.stealingdapenta.idletd.Idletd.isShuttingDown;
import static io.github.stealingdapenta.idletd.Idletd.logger;

@RequiredArgsConstructor
@Getter
@Setter
public class IdlePlayerManager {
    private static final Set<IdlePlayer> onlinePlayers = new HashSet<>();
    private static Cache<UUID, IdlePlayer> offlinePlayerCache = CacheBuilder.newBuilder().expireAfterAccess(20, TimeUnit.MINUTES).build();
    private static volatile Set<UUID> noLoginAllowed = new HashSet<>();
    private final IdlePlayerService idlePlayerService;
    private final AgentManager agentManager;
    private final TowerDefenseManager towerDefenseManager;
    private final TowerDefenseService towerDefenseService;

    public static Cache<UUID, IdlePlayer> getOfflinePlayerCache() {
        return offlinePlayerCache;
    }

    public static Set<UUID> getNoLoginAllowed() {
        return noLoginAllowed;
    }

    public boolean registerOnlinePlayer(IdlePlayer idlePlayer) {
        return onlinePlayers.add(idlePlayer);
    }

    public boolean deregisterOnlinePlayer(IdlePlayer idlePlayer) {
        return onlinePlayers.remove(idlePlayer);
    }

    public void postLogin(IdlePlayer idlePlayer) {
        // Generate player scoreboard
        // Generate stuff based on settings e.g. username
        // Handle lastLogin time etc
        idlePlayerService.getPlayer(idlePlayer.getPlayerUUID()).setGlowing(false);
        agentManager.activateAllAgents(idlePlayer);
    }

    public void postLogOut(Player player) {
        try {
            IdlePlayer idlePlayer = idlePlayerService.getIdlePlayer(player);
            if (!isShuttingDown()) {
                savePlayerData(idlePlayer);
                deactivateAndSaveTDGame(idlePlayer);
                agentManager.deactivateAndSaveAllAgents(idlePlayer);
            }
        } catch (Exception e) {
            logger.warning("Error in log out operations for " + player.getName());
        }
    }


    private void deactivateAndSaveTDGame(IdlePlayer idlePlayer) {
        TowerDefense towerDefense = towerDefenseManager.getActiveTDGame(idlePlayer);
        if (Objects.nonNull(towerDefense)) {
            towerDefenseManager.deactivateTDGame(towerDefense);
            towerDefenseService.updateTowerDefense(towerDefense);
        }
    }

    public void savePlayerData(IdlePlayer idlePlayer) {
        if (Objects.isNull(idlePlayer) || noLoginAllowed.contains(idlePlayer.getPlayerUUID())) {
            return;
        }

        // Protect data integrity by not allowing the player to log back in during this process.
        noLoginAllowed.add(idlePlayer.getPlayerUUID());

        try {
            idlePlayerService.updateIdlePlayer(idlePlayer);
        } catch (Exception e) {
            logger.warning("&eError saving user data for " + idlePlayerService.getPlayer(idlePlayer).getName());
            e.printStackTrace();
        } finally {
            if (!isShuttingDown()) {
                deregisterOnlinePlayer(idlePlayer);
                noLoginAllowed.remove(idlePlayer.getPlayerUUID());
            }
        }
    }
}
