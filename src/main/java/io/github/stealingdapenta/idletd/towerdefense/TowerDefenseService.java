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
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static io.github.stealingdapenta.idletd.service.utils.Time.ONE_SECOND_IN_TICKS;

@RequiredArgsConstructor
@Setter
@Getter
public class TowerDefenseService {
    private final TowerDefenseRepository towerDefenseRepository;
    private final PlotService plotService;
    private final IdlePlayerService idlePlayerService;
    private final SchematicHandler schematicHandler;
    private final Random random = new Random();
    private BukkitTask activeWave;

    public TowerDefense findTowerDefense(IdlePlayer idlePlayer) {
        TowerDefense towerDefense = getTowerDefense(idlePlayer.getPlayerUUID());
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

        // Countdown between waves can be made into a customizable setting todo
        Countdown.startCountdown(idlePlayerService.getPlayer(towerDefense.getPlayerUUID()), 5, ONE_SECOND_IN_TICKS, end -> {
            towerDefense.setWaveStartTime(System.currentTimeMillis());
            towerDefense.setWave(WaveConfiguration.getByLevel(towerDefense.getStageLevel()));

            idlePlayerService.getPlayer(towerDefense.getPlayerUUID()).sendMessage(">> Starting wave " + towerDefense.getStageLevel() + "!");

            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

            for (int i = 0; i < towerDefense.getWave().getNumMobs(); i++) {
                int finalI = i;
                long delayBetweenMobSummon = ONE_SECOND_IN_TICKS * i;


                scheduler.runTaskLater(Idletd.getInstance(), () -> {
                    CustomMob mob = generateRandomMob(towerDefense);
                    mob.summon(plot.getMobSpawnLocation());
                    towerDefense.addMob(mob);

                    // If this is the final iteration
                    if (finalI == towerDefense.getWave().getNumMobs() - 1) {
                        towerDefense.setWaveActive(false);
                    }
                }, delayBetweenMobSummon);
            }
        });
    }

    public void handleWaveEnd(TowerDefense towerDefense) {
        if (towerDefense.allMobsDead() && !towerDefense.isWaveActive()) {
            String duration = towerDefense.getWaveDuration();
            idlePlayerService.getPlayer(towerDefense.getPlayerUUID()).sendMessage("You took " + duration + " to complete wave " + towerDefense.getStageLevel() + "!");
            towerDefense.increaseStageLevelWithOne();
            startWave(towerDefense);
        }
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


    public void saveTowerDefense(TowerDefense towerDefense) {
        towerDefenseRepository.insertTowerDefense(towerDefense);
    }

    public void updateTowerDefense(TowerDefense towerDefense) {
        towerDefenseRepository.updateTowerDefense(towerDefense);
    }

    public TowerDefense getTowerDefense(UUID uuid) {
        return towerDefenseRepository.getTowerDefense(uuid);
    }
}
