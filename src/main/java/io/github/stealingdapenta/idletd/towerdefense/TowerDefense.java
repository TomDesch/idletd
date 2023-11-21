package io.github.stealingdapenta.idletd.towerdefense;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.plot.Plot;
import io.github.stealingdapenta.idletd.service.custommob.mobtypes.CustomMob;
import io.github.stealingdapenta.idletd.service.custommob.mobtypes.ZombieMob;
import io.github.stealingdapenta.idletd.service.utils.Countdown;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Setter
@Getter
public class TowerDefense {
    private final Random random = new Random();
    private final List<CustomMob> livingMobs = Collections.synchronizedList(new ArrayList<>());
    private int stageLevel;
    private WaveConfiguration wave;
    private Player player;
    private long waveStartTime;
    private boolean waveActive;
    private Plot plot;

    public TowerDefense(Player player) {
        stageLevel = 1;
        wave = WaveConfiguration.getByLevel(stageLevel);
        this.player = player;
        try {
            plot = Plot.getPlotByUUID(player.getUniqueId().toString());
        } catch (SQLException e) {
            Idletd.getInstance().getLogger().severe("Error finding player plot when starting TD game. (1)");
            Idletd.getInstance().getLogger().severe(e.getMessage());
        }
    }

    public TowerDefense(Player player, int level) {
        stageLevel = level;
        wave = WaveConfiguration.getByLevel(stageLevel);
        this.player = player;
        try {
            plot = Plot.getPlotByUUID(player.getUniqueId().toString());
        } catch (SQLException e) {
            Idletd.getInstance().getLogger().severe("Error finding player plot when starting TD game. (2)");
            Idletd.getInstance().getLogger().severe(e.getMessage());
        }
    }

    public void startWave() {
        setWaveActive(true);
        Countdown.startCountdown(player, 5, 20L, end -> {
            waveStartTime = System.currentTimeMillis();
            player.sendMessage(">> Starting wave " + getStageLevel() + "!"); // todo temporary
            createAsyncWaveTask();

            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

            for (int i = 0; i < wave.getNumMobs(); i++) {
                int finalI = i;  // Capture the value of i for each iteration
                long delayBetweenMobs = 20L * i; // 20 ticks = 1 second


                scheduler.runTaskLater(Idletd.getInstance(), () -> {
                    CustomMob mob = generateRandomMob();
                    mob.summon(plot.getMobSpawnLocation());
                    livingMobs.add(mob);

                    // If this is the final iteration
                    if (finalI == wave.getNumMobs() - 1) {
                        setWaveActive(false);
                    }
                }, delayBetweenMobs); // Delays summoning for each next mob to create a wave feeling.
            }
        });
    }

    private void createAsyncWaveTask() {
        // todo update in the future to have one task that loops over all active tower defense games

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isWaveEnd() && isWaveActive()) {
                    getWaveDuration(); // todo save
                    player.sendMessage("You took " + getWaveDuration() + " to complete wave " + getStageLevel() + "!"); // todo temporary
                    setStageLevel(getStageLevel() + 1);
                    startWave();
                    cancel();
                }

                Bukkit.getScheduler().runTaskAsynchronously(Idletd.getInstance(), () -> updateLivingMobs());
            }
        }.runTaskTimer(Idletd.getInstance(), 40L, 20L); // 20 ticks = 1 second
    }


    public String getWaveDuration() {
        // Calculate elapsed time in milliseconds
        long elapsedTimeMillis = System.currentTimeMillis() - waveStartTime;

        long seconds = (elapsedTimeMillis / 1000) % 60;
        long minutes = (elapsedTimeMillis / (1000 * 60)) % 60;
        long hours = elapsedTimeMillis / (1000 * 60 * 60);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void updateLivingMobs() {
        livingMobs.removeIf(customMob -> {
            Mob livingMob = customMob.getMob();
            if (Objects.nonNull(livingMob)) {
                return livingMob.isDead();
            }
            // theoretically, livingMobs can't contain customMobs without a Mob object, because they get summoned first, and
            // added to the list after. However, if the summoning failed for some reason, then it might be possible.
            // Removing them from the list then regardless.
            return true;

        });
    }

    private CustomMob generateRandomMob() {
        int mobLevel = 2 * stageLevel + random.nextInt((1 + stageLevel)); // todo Adjust as needed

        return switch (wave.chooseMobType()) {
            case ZOMBIE -> new ZombieMob(plot);
            default -> new ZombieMob(plot);
        };
    }

    public boolean isWaveEnd() {
        return livingMobs.isEmpty();
    }

}
