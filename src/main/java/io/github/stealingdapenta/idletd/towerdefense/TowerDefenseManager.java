package io.github.stealingdapenta.idletd.towerdefense;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob;
import io.github.stealingdapenta.idletd.custommob.mobtypes.SkeletonMob;
import io.github.stealingdapenta.idletd.custommob.mobtypes.ZombieMob;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.plot.Plot;
import io.github.stealingdapenta.idletd.plot.PlotService;
import io.github.stealingdapenta.idletd.service.utils.Countdown;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.github.stealingdapenta.idletd.Idletd.logger;
import static io.github.stealingdapenta.idletd.service.utils.Time.ONE_SECOND_IN_TICKS;

@RequiredArgsConstructor
@Getter
@Setter
public class TowerDefenseManager {
    private static final Set<TowerDefense> activeTDGames = new HashSet<>();
    private static BukkitTask activeGameManager;
    private final IdlePlayerService idlePlayerService;
    private final PlotService plotService;
    private final TowerDefenseService towerDefenseService;

    // todo turn POC wave mobs into real deal with levels on mobs etc

    public void initializeActiveGameManager() {
        if (Objects.isNull(activeGameManager)) {
            logger.info("Initializing Active TD Game manager.");
            activeGameManager = new BukkitRunnable() {
                @Override
                public void run() {
                    handleActiveGames();
                }
            }.runTaskTimer(Idletd.getInstance(), ONE_SECOND_IN_TICKS, ONE_SECOND_IN_TICKS / 2);
        }
    }

    public void activateTDGame(TowerDefense towerDefense) {
        addActiveTDGame(towerDefense);
        startWave(towerDefense);
    }

    private void handleActiveGames() {
        Set<TowerDefense> copyOfActiveGames = new HashSet<>(activeTDGames); // Defensive copy
        copyOfActiveGames.forEach(this::handlePlayerOffline);
        copyOfActiveGames.forEach(this::doWaveCycle);
    }

    private void doWaveCycle(TowerDefense towerDefense) {
        handleWaveEnd(towerDefense);
        towerDefense.updateLivingMobs();
    }

    public void addActiveTDGame(TowerDefense towerDefense) {
        activeTDGames.add(towerDefense);
    }

    public void deactivateTDGame(TowerDefense towerDefense) {
        logger.info("Deactivating TD Game for " + towerDefense.getPlayerUUID());
        boolean removed = activeTDGames.remove(towerDefense);
        logger.info("Removal of TD Games was " + (removed ? "successful." : "unsuccessful!"));
        towerDefense.setWaveActive(false);
        killAllMobs(towerDefense);
    }

    private void killAllMobs(TowerDefense towerDefense) {
        towerDefense.getLivingMobs().forEach(mob -> mob.getMob().remove());
    }

    public TowerDefense getActiveTDGame(IdlePlayer idlePlayer) {
        return activeTDGames.stream()
                            .filter(towerDefense -> idlePlayer.getPlayerUUID().equals(towerDefense.getPlayerUUID()))
                            .findFirst()
                            .orElse(null);
    }

    public void startWave(TowerDefense towerDefense) {
        towerDefense.setWaveActive(true);

        Plot plot = plotService.getPlot(towerDefense.getPlot());

        // Countdown between waves can be made into a customizable setting todo
        Countdown.startCountdown(idlePlayerService.getPlayer(towerDefense.getPlayerUUID()), 5, ONE_SECOND_IN_TICKS, end -> {
            towerDefense.setWaveStartTime(System.currentTimeMillis());
            towerDefense.setWave(new WaveConfiguration(towerDefense.getStageLevel()));

            // Event where player logs out during the countdown
            if (handlePlayerOffline(towerDefense)) return;

            idlePlayerService.getPlayer(towerDefense.getPlayerUUID()).sendMessage(">> Starting wave " + towerDefense.getStageLevel() + "!");

            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            int amountOfMobsToSpawn = towerDefense.getWave().getAmountOfMobs();

            for (int i = 0; i < amountOfMobsToSpawn; i++) {
                int finalI = i;
                long delayBetweenMobSummon = 2 * ONE_SECOND_IN_TICKS * i;

                scheduler.runTaskLater(Idletd.getInstance(), task -> {
                    // Event where player logs out during the countdown
                    if (handlePlayerOffline(towerDefense)) {
                        task.cancel();
                    }
                    CustomMob mob = generateRandomMob(towerDefense);
                    mob.summon(plot.getMobSpawnLocation());
                    mob.getMob().setTarget((LivingEntity) towerDefenseService.fetchTargetAgentIfNull(towerDefense).getAgentNPC().getNpc().getEntity());
                    towerDefense.addMob(mob);

                    if (isFinalIteration(finalI, amountOfMobsToSpawn)) {
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
        int mobLevel = towerDefense.getWave().getRandomMobLevelBasedOnStageLevel();

        Plot plot = towerDefenseService.fetchPlotIfNull(towerDefense);

        return switch (towerDefense.getWave().chooseMobType()) {
            case ZOMBIE -> new ZombieMob(plot, mobLevel);
            case SKELETON -> new SkeletonMob(plot, mobLevel);
            default -> new ZombieMob(plot, mobLevel);
        };
    }

    private boolean isFinalIteration(int iteration, int amountOfMobsToSpawn) {
        return iteration == (amountOfMobsToSpawn - 1);
    }

    private boolean handlePlayerOffline(TowerDefense towerDefense) {
        Player player = Idletd.getInstance().getServer().getPlayer(towerDefense.getPlayerUUID());
        if (Objects.isNull(player)) {
            logger.warning("Player has an active TD Game but is not online: " + towerDefense.getPlayerUUID());
            deactivateTDGame(towerDefense);
            return true;
        }
        return false;
    }
}
