package io.github.stealingdapenta.idletd.service.command;

import io.github.stealingdapenta.idletd.service.custommob.CustomMobSpawner;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class SpawnZombieCommand implements CommandExecutor {

    private final CustomMobSpawner customMobSpawner;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("Summoning zombie");
        Player player = (Player) commandSender;

        this.customMobSpawner.spawnInFrontOfPlayer(player, 10);
        return true;
    }
}
