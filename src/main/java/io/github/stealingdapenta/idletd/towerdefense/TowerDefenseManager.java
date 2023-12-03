package io.github.stealingdapenta.idletd.towerdefense;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.github.stealingdapenta.idletd.service.utils.Time.ONE_SECOND_IN_TICKS;

@RequiredArgsConstructor
@Getter
@Setter
public class TowerDefenseManager {
    private static final Set<TowerDefense> activeTDGames = new HashSet<>();
    private static BukkitTask activeGameManager;
    private final IdlePlayerService idlePlayerService;
    private final TowerDefenseService towerDefenseService;

    // todo
    // store active games upon activating (future: setting auto activate upon login? )
    //
    // upon log out: deactivate game & kill all the mobs AFTER (so no lvl increase or gold income)
    // Save the game state

    // todo further: summon NPC for target
    // todo further: fix "endless mode" on waves without crashing
    // then turn POC wave mobs into real deal with levels on mobs etc
    // todo update in the future to have one task that loops over all active tower defense games
    // todo Make waves generate infinite new ones; e.g. by taking re-looping over the fixed enum & multiplying the mob levels (using modulo) <= CRUCIAL

    public void initializeActiveGameManager() {
        if (Objects.isNull(activeGameManager)) {
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
        towerDefenseService.startWave(towerDefense);
    }

    private void handleActiveGames() {
        activeTDGames.forEach(this::doWaveCycle);
    }

    private void doWaveCycle(TowerDefense towerDefense) {
        towerDefenseService.handleWaveEnd(towerDefense);
        towerDefense.updateLivingMobs();
    }

    public boolean addActiveTDGame(TowerDefense towerDefense) {
        return activeTDGames.add(towerDefense);
    }

    public boolean deactivateTDGame(TowerDefense towerDefense) {
        towerDefense.setWaveActive(false);
        killAllMobs(towerDefense);
        return activeTDGames.remove(towerDefense);
    }

    private void killAllMobs(TowerDefense towerDefense) {
        towerDefense.getLivingMobs().forEach(mob -> mob.getMob().remove());
    }

    public boolean hasActiveTDGame(IdlePlayer idlePlayer) {
        return activeTDGames.stream().anyMatch(towerDefense -> towerDefense.getPlayerUUID().equals(idlePlayer.getPlayerUUID()));
    }

    public TowerDefense getActiveTDGame(IdlePlayer idlePlayer) {
        return activeTDGames.stream()
                            .filter(towerDefense -> idlePlayer.getPlayerUUID().equals(towerDefense.getPlayerUUID()))
                            .findFirst()
                            .orElse(null);
    }
}
