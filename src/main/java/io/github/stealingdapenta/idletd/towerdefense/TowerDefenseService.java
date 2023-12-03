package io.github.stealingdapenta.idletd.towerdefense;

import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import io.github.stealingdapenta.idletd.plot.Plot;
import io.github.stealingdapenta.idletd.plot.PlotService;
import io.github.stealingdapenta.idletd.service.utils.SchematicHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.scheduler.BukkitTask;

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

    public Plot fetchPlotIfNull(TowerDefense towerDefense) {
        if (Objects.isNull(towerDefense.getFetchedPlot())) {
            towerDefense.setFetchedPlot(plotService.getPlot(towerDefense.getPlot()));
        }
        return towerDefense.getFetchedPlot();
    }

    public IdlePlayer fetchIdlePlayerIfNull(TowerDefense towerDefense) {
        if (Objects.isNull(towerDefense.getFetchedPlayer())) {
            towerDefense.setFetchedPlayer(idlePlayerService.getIdlePlayer(towerDefense.getPlayerUUID()));
        }
        return towerDefense.getFetchedPlayer();
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
