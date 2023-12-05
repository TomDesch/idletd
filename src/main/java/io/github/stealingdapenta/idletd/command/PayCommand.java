package io.github.stealingdapenta.idletd.command;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerManager;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.idleplayer.stats.BalanceHandler;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

import static io.github.stealingdapenta.idletd.Idletd.logger;

@RequiredArgsConstructor
public class PayCommand implements CommandExecutor {
    private static final String NO_IDLE_PLAYER = "Internal error. Please contact a system admin.";
    private static final String NO_IDLE_TARGET = "Error finding target player. Please put a valid player name!";
    private static final String BALANCE = "Successful payment! Your balance of Guard now stands at %s ₰";
    private static final String FORMAT_ERROR = "Error formatting the amount! Please put a valid number.";
    private static final String PAY_TO_SELF = "Paying yourself is like giving yourself a high five... Are you okay?";
    private static final String INSUFFICIENT_FUNDS = "Insufficient funds!";
    private final IdlePlayerService idlePlayerService;
    private final IdlePlayerManager idlePlayerManager;
    private final BalanceHandler balanceHandler;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player player = (Player) commandSender;
        IdlePlayer idlePlayer = idlePlayerManager.getOnlineIdlePlayer(player);

        if (Objects.isNull(idlePlayer)) {
            logger.severe(player.getName() + " doesn't have a linked IdlePlayer online.");
            player.sendMessage(Component.text(NO_IDLE_PLAYER));
            return true;
        }

        if (args.length < 2) return false;

        double amount;
        String target = args[0];
        Player targetPlayer = Idletd.getInstance().getServer().getPlayer(target);

        IdlePlayer idleTarget;
        if (Objects.isNull(targetPlayer)) {
            UUID uuid = Idletd.getInstance().getServer().getOfflinePlayer(target).getUniqueId();
            idleTarget = idlePlayerService.getIdlePlayer(uuid);
        } else {
            idleTarget = idlePlayerService.getIdlePlayer(targetPlayer);
        }

        if (Objects.isNull(idleTarget)) {
            player.sendMessage(NO_IDLE_TARGET);
            return false;
        }

        boolean targetIsOnline = idlePlayerManager.isOnline(idleTarget);
        if (targetIsOnline) {
            idleTarget = idlePlayerManager.getOnlineIdlePlayer(idleTarget.getPlayerUUID());
        }

        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(FORMAT_ERROR);
            return false;
        }

        if (idlePlayer.equals(idleTarget)) {
            player.sendMessage(PAY_TO_SELF);
            return true;
        }

        boolean successfulTransfer = balanceHandler.pay(idlePlayer, idleTarget, amount);
        if (successfulTransfer) {
            player.sendMessage(BALANCE.formatted(idlePlayer.getBalance()));

            if (!targetIsOnline) {
                idlePlayerService.saveIdlePlayer(idleTarget);
            }
        } else {
            player.sendMessage(INSUFFICIENT_FUNDS);
        }
        return true;
    }
}