package io.github.stealingdapenta.idletd.idleplayer;

import io.github.stealingdapenta.idletd.Idletd;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

import static io.github.stealingdapenta.idletd.Idletd.logger;

@RequiredArgsConstructor
public class IdlePlayerService {
    private final IdlePlayerRepository idlePlayerRepository;

    public Player getPlayer(IdlePlayer idlePlayer) {
        return getPlayer(idlePlayer.getPlayerUUID());
    }

    public Player getPlayer(String uuid) {
        return getPlayer(UUID.fromString(uuid));
    }

    public Player getPlayer(UUID uuid) {
        return Idletd.getInstance().getServer().getPlayer(uuid);
    }

    public IdlePlayer getIdlePlayer(Player player) {
        return getIdlePlayer(player.getUniqueId());
    }

    public IdlePlayer getIdlePlayer(UUID uuid) {
        return idlePlayerRepository.getIdlePlayer(uuid);
    }

    public IdlePlayer createNewIdlePlayer(UUID uuid) {
        logger.info("Generating new IdlePlayer for " + uuid);

        IdlePlayer idlePlayer = IdlePlayer.builder()
                                          .playerUUID(uuid)
                                          .balance(0)
                                          .build();

        logger.info("Saving new idlePlayer.");
        saveIdlePlayer(idlePlayer);

        return idlePlayer;
    }

    public void saveIdlePlayer(IdlePlayer idlePlayer) {
        idlePlayerRepository.saveIdlePlayer(idlePlayer);
    }

    public void updateIdlePlayer(IdlePlayer idlePlayer) {
        idlePlayerRepository.updateIdlePlayer(idlePlayer);
    }
}
