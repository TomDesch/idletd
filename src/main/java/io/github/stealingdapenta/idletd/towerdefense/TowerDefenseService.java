package io.github.stealingdapenta.idletd.towerdefense;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.plot.Plot;
import io.github.stealingdapenta.idletd.plot.PlotService;
import io.github.stealingdapenta.idletd.service.custommob.mobtypes.CustomMob;
import io.github.stealingdapenta.idletd.service.custommob.mobtypes.SkeletonMob;
import io.github.stealingdapenta.idletd.service.custommob.mobtypes.ZombieMob;
import io.github.stealingdapenta.idletd.service.utils.Countdown;
import io.github.stealingdapenta.idletd.service.utils.SchematicHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Setter
@Getter
public class TowerDefenseService {
    private final TowerDefenseRepository towerDefenseRepository;
    private final PlotService plotService;
    private final IdlePlayerService idlePlayerService;
    private final List<CustomMob> livingMobs = new ArrayList<>();
    private final SchematicHandler schematicHandler;
    private final Random random = new Random();

    public TowerDefense findTowerDefense(IdlePlayer idlePlayer) {
        TowerDefense towerDefense = towerDefenseRepository.getTowerDefense(idlePlayer.getPlayerUUID());
        if (Objects.nonNull(towerDefense)) {
            fetchPlotIfNull(towerDefense);
            fetchIdlePlayerIfNull(towerDefense);
        }

        return towerDefense;
    }

    private void fetchPlotIfNull(TowerDefense towerDefense) {
        if (Objects.isNull(towerDefense.getFetchedPlot())) {
            towerDefense.setFetchedPlot(plotService.getPlot(towerDefense.getPlot()));
        }
    }

    private void fetchIdlePlayerIfNull(TowerDefense towerDefense) {
        if (Objects.isNull(towerDefense.getFetchedPlayer())) {
            towerDefense.setFetchedPlayer(idlePlayerService.getIdlePlayer(towerDefense.getPlayerUUID()));
        }
    }

    public void startWave(TowerDefense towerDefense) {
        towerDefense.setWaveActive(true);

        Plot plot = plotService.getPlot(towerDefense.getPlot());

        Countdown.startCountdown(idlePlayerService.getPlayer(towerDefense.getPlayerUUID()), 5, 20L, end -> {
            towerDefense.setWaveStartTime(System.currentTimeMillis());
            towerDefense.setWave(WaveConfiguration.getByLevel(towerDefense.getStageLevel()));
            idlePlayerService.getPlayer(towerDefense.getPlayerUUID()).sendMessage(">> Starting wave " + towerDefense.getStageLevel() + "!"); // todo temporary
            createAsyncWaveTask(towerDefense);

            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

            for (int i = 0; i < towerDefense.getWave().getNumMobs(); i++) {
                int finalI = i;  // Capture the value of i for each iteration
                long delayBetweenMobs = 20L * i; // 20 ticks = 1 second


                scheduler.runTaskLater(Idletd.getInstance(), () -> {
                    CustomMob mob = generateRandomMob(towerDefense);
                    mob.summon(plot.getMobSpawnLocation());
                    livingMobs.add(mob);

                    // If this is the final iteration
                    if (finalI == towerDefense.getWave().getNumMobs() - 1) {
                        towerDefense.setWaveActive(false);
                    }
                }, delayBetweenMobs); // Delays summoning for each next mob to create a wave feeling.
            }
        });
    }

    private void createAsyncWaveTask(TowerDefense towerDefense) {
        // todo update in the future to have one task that loops over all active tower defense games

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isWaveEnd() && !towerDefense.isWaveActive()) {
                    towerDefense.getWaveDuration();
                    idlePlayerService.getPlayer(towerDefense.getPlayerUUID()).sendMessage("You took " + towerDefense.getWaveDuration() + " to complete wave " + towerDefense.getStageLevel() + "!");
                    // todo temporary
                    towerDefense.increaseStageLevelWithOne();

                    // todo Make waves generate infinite new ones; e.g. by taking re-looping over the fixed enum & multiplying the mob levels (using modulo) <= CRUCIAL
                    startWave(towerDefense);
                    cancel();
                }

                Bukkit.getScheduler().runTaskAsynchronously(Idletd.getInstance(), () -> updateLivingMobs());
            }
        }.runTaskTimer(Idletd.getInstance(), 40L, 20L); // 20 ticks = 1 second
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

    private CustomMob generateRandomMob(TowerDefense towerDefense) {
//        int mobLevel = 2 * towerDefense.getStageLevel() + random.nextInt((1 + towerDefense.getStageLevel())); // todo Adjust as needed

        Plot plot = plotService.getPlot(towerDefense.getPlot());

        return switch (towerDefense.getWave().chooseMobType()) {
            case ZOMBIE -> new ZombieMob(plot);
            case SKELETON -> new SkeletonMob(plot);
            default -> new ZombieMob(plot);
        };
    }

    public boolean isWaveEnd() {
        return livingMobs.isEmpty();
    }

    public void saveTowerDefense(TowerDefense towerDefense) {
        towerDefenseRepository.insertTowerDefense(towerDefense);
    }

    public TowerDefense getTowerDefense(UUID uuid) {
        return towerDefenseRepository.getTowerDefense(uuid);
    }
}
