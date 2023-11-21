package io.github.stealingdapenta.idletd.towerdefense;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.service.custommob.mobtypes.CustomMob;
import io.github.stealingdapenta.idletd.service.custommob.mobtypes.ZombieMob;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Setter
@Getter
public class TowerDefense {
    private final Random random = new Random();
    private final List<CustomMob> livingMobs = new ArrayList<>();
    private int stageLevel;
    private WaveConfiguration wave;
    private Player player;
    private long waveStartTime;
    private boolean waveActive;


    public TowerDefense(Player player) {
        stageLevel = 1;
        wave = WaveConfiguration.getByLevel(stageLevel);
        this.player = player;
    }

    public TowerDefense(Player player, int level) {
        stageLevel = level;
        wave = WaveConfiguration.getByLevel(stageLevel);
        this.player = player;
    }

    public void startWave() {
        setWaveActive(true);
        waveStartTime = System.currentTimeMillis();
        createAsyncWaveTask();

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        for (int i = 0; i < wave.getNumMobs(); i++) {
            int finalI = i;  // Capture the value of i for each iteration
            long delayBetweenMobs = 20L * i; // 20 ticks = 1 second


            scheduler.runTaskLater(Idletd.getInstance(), () -> {
                CustomMob mob = generateRandomMob();
                livingMobs.add(mob);

                // If this is the final iteration
                if (finalI == wave.getNumMobs() - 1) {
                    setWaveActive(false);
                }
            }, delayBetweenMobs); // Delays summoning for each next mob to create a wave feeling.
        }
    }


    private void createAsyncWaveTask() {
        // todo update in the future to have one task that loops over all active tower defense games

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isWaveEnd() && isWaveActive()) {
                    getWaveDuration(); // todo display / save
                    setStageLevel(getStageLevel() + 1);
                    // todo countdown
                    startWave();
                    cancel();
                }

                Bukkit.getScheduler().runTaskAsynchronously(Idletd.getInstance(), () -> updateLivingMobs());
            }
        }.runTaskTimer(Idletd.getInstance(), 0L, 20L); // 20 ticks = 1 second
    }


    public String getWaveDuration() {
        // Calculate elapsed time in milliseconds
        long elapsedTimeMillis = System.currentTimeMillis() - waveStartTime;

        // Convert milliseconds to hh:mm:ss format
        long seconds = (elapsedTimeMillis / 1000) % 60;
        long minutes = (elapsedTimeMillis / (1000 * 60)) % 60;
        long hours = elapsedTimeMillis / (1000 * 60 * 60);

        // Format the result
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void updateLivingMobs() {
        livingMobs.removeIf(mob -> mob.getMob().isDead());
    }


    private CustomMob generateRandomMob() {
        int mobLevel = 2 * stageLevel + random.nextInt((1 + stageLevel)); // Adjust as needed

        // todo track if they're alive or dead
        // todo if all dead: countdown to next wave.

        return switch (wave.chooseMobType()) {
            case ZOMBIE -> new ZombieMob();
            default -> new ZombieMob();
        };
    }


    // Method to check if the wave has ended
    public boolean isWaveEnd() {
        return livingMobs.isEmpty();
    }

}
