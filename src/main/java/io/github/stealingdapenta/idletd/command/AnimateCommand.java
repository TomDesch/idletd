package io.github.stealingdapenta.idletd.command;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.custommob.AttackAnimationHandler;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.scheduler.BukkitRunnable;
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

        entityCollection.forEach(entity -> {
            if (entity instanceof Skeleton skeleton) {
                makeSkeletonDrawBow(skeleton, player.getEyeLocation());
            } else {
                attackAnimationHandler.sendMeleeAttackAnimation(player, entity);
            }
        });

        return true;
    }

    private void makeSkeletonDrawBow(Skeleton skeleton, Location targetLocation) {
        ArmorStand invisibleTarget = createArmorStand(targetLocation);
        skeleton.setTarget(invisibleTarget);
        new BukkitRunnable() {
            @Override
            public void run() {
                invisibleTarget.remove();
                skeleton.setTarget(null);
            }
        }.runTaskLater(Idletd.getInstance(), 40L); // 20 ticks = 1 second
    }

    private ArmorStand createArmorStand(Location location) {
        return location.getWorld().spawn(location, ArmorStand.class, armorStand -> {
            armorStand.setVisible(false);
            armorStand.setCollidable(false);
            armorStand.setInvulnerable(true);
            armorStand.setMarker(true);
            armorStand.setGravity(false);
        });
    }
}