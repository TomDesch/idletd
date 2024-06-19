package io.github.stealingdapenta.idletd.command;

import static io.github.stealingdapenta.idletd.Idletd.LOGGER;

import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerManager;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class BalanceCommand implements CommandExecutor {
    private static final String NO_IDLE_PLAYER = "Internal error. Please contact a system admin.";
    private static final String BALANCE = "Your balance of Guard stands at %s ₰";
    private final IdlePlayerManager idlePlayerManager;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player player = (Player) commandSender;
        IdlePlayer idlePlayer = idlePlayerManager.getOnlineIdlePlayer(player);

        if (Objects.isNull(idlePlayer)) {
            LOGGER.severe(player.getName() + " doesn't have a linked IdlePlayer or is not recognized as online.");
            player.sendMessage(Component.text(NO_IDLE_PLAYER));
            return true;
        }

        player.sendMessage(BALANCE.formatted(idlePlayer.getBalance()));
        return true;
    }
}