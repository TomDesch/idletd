package io.github.stealingdapenta.idletd.command;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.idleplayer.stats.BalanceHandler;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static io.github.stealingdapenta.idletd.Idletd.logger;

@RequiredArgsConstructor
public class PayCommand implements CommandExecutor {
    private static final String NO_IDLE_PLAYER = "Internal error. Please contact a system admin.";
    private static final String NO_IDLE_TARGET = "Error finding target player. Please put a valid player name!";
    private static final String BALANCE = "Successful payment! Your balance of Guard now stands at %s ₰";
    private static final String FORMAT_ERROR = "Error formatting the amount! Please put a valid number.";
    private static final String INSUFFICIENT_FUNDS = "Insufficient funds!";
    private final IdlePlayerService idlePlayerService;
    private final BalanceHandler balanceHandler;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player player = (Player) commandSender;
        IdlePlayer idlePlayer = idlePlayerService.getIdlePlayer(player);

        if (Objects.isNull(idlePlayer)) {
            logger.severe(player.getName() + " doesn't have a linked IdlePlayer.");
            player.sendMessage(Component.text(NO_IDLE_PLAYER));
            return true;
        }

        if (args.length < 2) return false;

        double amount;
        Player targetPlayer = Idletd.getInstance().getServer().getPlayer(args[0].toLowerCase());
        IdlePlayer target = idlePlayerService.getIdlePlayer(targetPlayer);

        if (Objects.isNull(target)) {
            player.sendMessage(NO_IDLE_TARGET);
            return false;
        }

        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(FORMAT_ERROR);
            return false;
        }

        boolean successfulTransfer = balanceHandler.pay(idlePlayer, target, amount);
        if (successfulTransfer) {
            player.sendMessage(BALANCE.formatted(idlePlayer.getBalance()));
        } else {
            player.sendMessage(INSUFFICIENT_FUNDS);
        }
        return true;
    }
}