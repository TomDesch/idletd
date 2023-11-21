package io.github.stealingdapenta.idletd.service.utils;

import io.github.stealingdapenta.idletd.Idletd;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class Countdown extends BukkitRunnable {
    private final Player player;
    private final BossBar bossBar;
    private final Consumer<Player> onCountdownEnd;
    private int secondsRemaining;

    public Countdown(Player player, int seconds, Consumer<Player> onCountdownEnd) {
        this.player = player;
        this.secondsRemaining = seconds;
        this.onCountdownEnd = onCountdownEnd;

        // Create a boss bar for the player
        this.bossBar = Bukkit.createBossBar("Commencing countdown!", BarColor.BLUE, BarStyle.SEGMENTED_10);
        bossBar.addPlayer(player);
    }

    public static void startCountdown(Player player, int time, long delay, Consumer<Player> onCountdownEnd) {
        Countdown countdownTask = new Countdown(player, time, onCountdownEnd);
        countdownTask.runTaskTimer(Idletd.getInstance(), delay, 20L);
    }

    @Override
    public void run() {
        if (secondsRemaining > 0) {
            // Update boss bar with countdown message
            bossBar.setTitle("Time remaining: " + secondsRemaining + " seconds");
            bossBar.setProgress(secondsRemaining / 5.0);
            secondsRemaining--;
        } else {
            // Countdown complete, execute the callback and cancel the task
            bossBar.removeAll();
            onCountdownEnd.accept(player);
            cancel();
        }
    }
}
