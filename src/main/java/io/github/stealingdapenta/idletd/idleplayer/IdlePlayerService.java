package io.github.stealingdapenta.idletd.idleplayer;

import io.github.stealingdapenta.idletd.Idletd;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class IdlePlayerService {
    private static final Logger logger = Idletd.getInstance().getLogger();
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

}
