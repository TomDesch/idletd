package io.github.stealingdapenta.idletd.idleplayer;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.plot.Plot;
import io.github.stealingdapenta.idletd.plot.PlotService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

import static io.github.stealingdapenta.idletd.Idletd.logger;

@RequiredArgsConstructor
public class IdlePlayerService {
    private static final String ERROR_CONVERTING_UUID = "Failed to convert string to UUID ' %s ' in IdlePlayerService.";
    private final IdlePlayerRepository idlePlayerRepository;
    private final PlotService plotService;

    public Player getPlayer(IdlePlayer idlePlayer) {
        return getPlayer(idlePlayer.getPlayerUUID());
    }

    public Player getPlayer(String uuid) {
        try {
            return getPlayer(UUID.fromString(uuid));
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warning(ERROR_CONVERTING_UUID.formatted(uuid));
            return null;
        }
    }

    public Player getPlayer(UUID uuid) {
        return Idletd.getInstance().getServer().getPlayer(uuid);
    }

    public IdlePlayer getIdlePlayer(Player player) {
        return getIdlePlayer(player.getUniqueId());
    }

    public IdlePlayer getIdlePlayer(String uuidString) {
        try {
            UUID uuid = UUID.fromString(uuidString);
            return getIdlePlayer(uuid);
        } catch (IllegalArgumentException e) {
            logger.warning(ERROR_CONVERTING_UUID.formatted(uuidString));
            return null;
        }
    }

    public IdlePlayer getIdlePlayer(UUID uuid) {
        return idlePlayerRepository.getIdlePlayer(uuid);
    }

    public IdlePlayer createNewIdlePlayer(UUID uuid) {
        logger.info("Generating new IdlePlayer for " + uuid);

        // Normally impossible to already have a plot...
        Plot existingPlot = plotService.findPlot(uuid);

        IdlePlayer idlePlayer = IdlePlayer.builder()
                                          .playerUUID(uuid)
                                          .fkPlot(Objects.nonNull(existingPlot) ? existingPlot.getId() : null)
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
