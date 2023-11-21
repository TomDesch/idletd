package io.github.stealingdapenta.idletd.service.command;

import io.github.stealingdapenta.idletd.towerdefense.TowerDefense;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class TowerDefenseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("Starting TD game");
        Player player = (Player) commandSender;


        // todo check if the player already has a game, only one game per player, if so, dont start a new one
        TowerDefense towerDefense = new TowerDefense(player);

        // todo check if player has a saved game, if so, start it from the game save level
        // todo save the game regularly
        // todo calculate AFK income (idle)

        // todo add economy, reward per kill, bank value

        // todo voor afk gold income, misschien "highest income op een round" + "duur round" saven en dan percentage daarvan per minuut uitbetalen met zelfde verhouding
        towerDefense.startWave();
        return true;
    }
}

