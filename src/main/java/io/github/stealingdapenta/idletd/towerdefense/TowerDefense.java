package io.github.stealingdapenta.idletd.towerdefense;

import io.github.stealingdapenta.idletd.agent.Agent;
import io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.plot.Plot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TowerDefense {
    // table fields
    private final UUID playerUUID;
    private long plot;
    private int stageLevel;

    // calculated fields
    private WaveConfiguration wave;
    private long waveStartTime;
    private boolean waveActive;
    private Plot fetchedPlot;
    private IdlePlayer fetchedPlayer;
    private Agent targetAgent;
    private List<CustomMob> livingMobs = new ArrayList<>();

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

    public boolean allMobsDead() {
        initializeLivingMobs();
        return livingMobs.isEmpty();
    }

    public void updateLivingMobs() {
        initializeLivingMobs();
        getLivingMobs().removeIf(customMob -> {
            Mob livingMob = customMob.getMob();
            if (Objects.nonNull(livingMob)) {
                return !livingMob.isValid();
            }
            // theoretically, livingMobs can't contain customMobs without a Mob object, because they get summoned first, and
            // added to the list after. However, if the summoning failed for some reason, then it might be possible.
            // Removing them from the list then regardless.
            return true;
        });
        getLivingMobs().forEach(customMob -> customMob.getMob().setTarget((LivingEntity) getTargetAgent().getAgentNPC().getNpc().getEntity()));
    }

    public void addMob(CustomMob mob) {
        initializeLivingMobs();
        livingMobs.add(mob);
    }

    private void initializeLivingMobs() {
        if (Objects.isNull(livingMobs)) {
            livingMobs = new ArrayList<>();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TowerDefense that = (TowerDefense) o;
        return Objects.equals(playerUUID, that.playerUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerUUID);
    }
}
