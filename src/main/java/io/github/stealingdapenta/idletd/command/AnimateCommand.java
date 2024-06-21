package io.github.stealingdapenta.idletd.command;

import io.github.stealingdapenta.idletd.custommob.AttackAnimationHandler;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class AnimateCommand implements CommandExecutor {

    private final AttackAnimationHandler attackAnimationHandler;

    /**
     * This method is a DEBUG method that will be removed later.
     */
    @Deprecated(forRemoval = true)
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Only players can use this command.");
            return true;
        }

        player.sendMessage("Animating all nearby monsters...");

        Collection<Entity> entityCollection = player.getLocation().getNearbyEntities(50, 50, 50);

        if (entityCollection.isEmpty()) {
            player.sendMessage("No entities found nearby.");
            return false;
        }

        entityCollection.forEach(entity -> attackAnimationHandler.sendMeleeAttackAnimation(player, entity));

        return true;
    }
}