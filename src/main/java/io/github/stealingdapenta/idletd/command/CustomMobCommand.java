package io.github.stealingdapenta.idletd.command;

import io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob;
import io.github.stealingdapenta.idletd.custommob.mobtypes.SkeletonMob;
import io.github.stealingdapenta.idletd.custommob.mobtypes.ZombieMob;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

@RequiredArgsConstructor
public class CustomMobCommand implements CommandExecutor {


    public static Location getLocationOnTopOfBlock(Player player) {
        BlockIterator blockIterator = new BlockIterator(player, 5); // Adjust the range as needed

        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (!block.isEmpty() && !block.isLiquid()) {
                return block.getLocation()
                            .add(0.5, 1.0, 0.5); // Adjust the offset as needed
            }
        }

        // Default to player's location if no block is found
        return player.getLocation();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player sourcePlayer = (Player) commandSender;

        if (args.length < 2) {
            return false;
        }

        String typeString = args[0];
        String levelString = args[1];
        int level;

        try {
            level = Integer.parseInt(levelString);
        } catch (NumberFormatException e) {
            sourcePlayer.sendMessage("Invalid level.");
            return false;
        }

        CustomMob customMob;

        int amount = 1;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            sourcePlayer.sendMessage("No/wrong amount specified, defaulting 1.");
        }

        for (int i = 0; i < amount; i++) {
            switch (typeString.toLowerCase()) {
                case "zombie":
                    customMob = new ZombieMob(null, level);
                case "skeleton":
                    customMob = new SkeletonMob(null, level);
                default:
                    customMob = new ZombieMob(null, level);
            }

            customMob.summon(getLocationOnTopOfBlock(sourcePlayer));
            customMob.getMob()
                     .setTarget(sourcePlayer);
        }

        return true;
    }
}