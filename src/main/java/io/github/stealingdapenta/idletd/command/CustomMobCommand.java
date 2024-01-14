package io.github.stealingdapenta.idletd.command;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.agent.Agent;
import io.github.stealingdapenta.idletd.agent.AgentManager;
import io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob;
import io.github.stealingdapenta.idletd.custommob.mobtypes.SkeletonMob;
import io.github.stealingdapenta.idletd.custommob.mobtypes.ZombieMob;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerManager;
import io.github.stealingdapenta.idletd.plot.Plot;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BlockIterator;

@RequiredArgsConstructor
public class CustomMobCommand implements CommandExecutor {

    private static final String CUSTOM_SUMMON = "customsummon";
    private static final String CONTAINS_AGENT = "Successfully summoned the mob(s) with agent %s selected.";
    private static final String NO_IDLE_PLAYER = "No linked IdlePlayer found.";
    private static final String WRONG_AMOUNT = "No/wrong amount specified, defaulting 1.";
    private static final String KILLING_ALL = "Killing all custom summoned monsters nearby you, radius: %s.";
    private static final String INVALID_LEVEL = "Invalid level or radius.";
    private static final String ZOMBIE_STRING = "zombie";
    private static final String SKELETON_STRING = "skeleton";
    private final IdlePlayerManager idlePlayerManager;
    private final AgentManager agentManager;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player sourcePlayer = (Player) commandSender;

        IdlePlayer onlineIdlePlayer = idlePlayerManager.getOnlineIdlePlayer(sourcePlayer);

        if (Objects.isNull(onlineIdlePlayer)) {
            sourcePlayer.sendMessage(NO_IDLE_PLAYER);
            return false;
        }

        if (args.length < 2) {
            return false;
        }

        String firstArg = args[0];
        String secondArg = args[1];
        int level;

        try {
            level = Integer.parseInt(secondArg);
        } catch (NumberFormatException e) {
            sourcePlayer.sendMessage(INVALID_LEVEL);
            return false;
        }

        if ("killall".equals(firstArg)) {
            sourcePlayer.sendMessage(KILLING_ALL.formatted(level));
            sourcePlayer.getLocation()
                        .getNearbyEntities(level, level, level)
                        .stream()
                        .filter(this::isCustomSummoned)
                        .forEach(org.bukkit.entity.Entity::remove);
            return true;
        }

        int amount = 1;
        try {
            if (args.length > 2) {
                amount = Integer.parseInt(args[2]);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            sourcePlayer.sendMessage(WRONG_AMOUNT);
        }

        Plot fictionalPlot = new Plot(0, 0, 0, sourcePlayer.getUniqueId());

        for (int i = 0; i < amount; i++) {
            CustomMob customMob = switch (firstArg.toLowerCase()) {
                case ZOMBIE_STRING -> new ZombieMob(fictionalPlot, level);
                case SKELETON_STRING -> new SkeletonMob(fictionalPlot, level);
                default -> new ZombieMob(fictionalPlot, level);
            };

            Agent activeMainAgent = agentManager.getActiveMainAgent(onlineIdlePlayer);

            customMob.summon(getLocationOnTopOfBlock(sourcePlayer), activeMainAgent);
            sourcePlayer.sendMessage(TextColor.color(19, 147, 56) + CONTAINS_AGENT.formatted(TextColor.color(56, 255, 33) + activeMainAgent.toString()));

            customMob.getMob()
                     .getPersistentDataContainer()
                     .set(getCustomSummonKey(), PersistentDataType.BOOLEAN, true);
        }

        return true;
    }

    private NamespacedKey getCustomSummonKey() {
        return new NamespacedKey(Idletd.getInstance(), CUSTOM_SUMMON);
    }

    public boolean isCustomSummoned(Entity entity) {
        return Boolean.TRUE.equals(entity.getPersistentDataContainer()
                                         .get(getCustomSummonKey(), PersistentDataType.BOOLEAN));
    }

    public static Location getLocationOnTopOfBlock(Player player) {
        BlockIterator blockIterator = new BlockIterator(player, 50);

        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (!block.getType()
                      .isAir() && !block.isLiquid()) {
                return block.getLocation()
                            .add(0.5, 1.0, 0.5);
            }
        }

        return player.getEyeLocation()
                     .add(0, 1, 0);
    }
}