package io.github.stealingdapenta.idletd.towerdefense;

import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.plot.Plot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TowerDefense {
    // table fields
    private UUID playerUUID;
    private long plot;
    private int stageLevel;

    // calculated fields
    private WaveConfiguration wave;
    private long waveStartTime;
    private boolean waveActive;
    private Plot fetchedPlot;
    private IdlePlayer fetchedPlayer;

    public String getWaveDuration() {
        // Calculate elapsed time in milliseconds
        long elapsedTimeMillis = System.currentTimeMillis() - waveStartTime;

        long seconds = (elapsedTimeMillis / 1000) % 60;
        long minutes = (elapsedTimeMillis / (1000 * 60)) % 60;
        long hours = elapsedTimeMillis / (1000 * 60 * 60);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void increaseStageLevelWithOne() {
        setStageLevel(getStageLevel() + 1);
    }
}
