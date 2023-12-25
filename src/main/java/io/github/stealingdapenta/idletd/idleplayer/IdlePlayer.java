package io.github.stealingdapenta.idletd.idleplayer;

import io.github.stealingdapenta.idletd.idleplayer.battlestats.BattleStats;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class IdlePlayer {

    private UUID playerUUID;
    private double balance;
    private Long fkPlot;

    private BattleStats fetchedBattleStats;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdlePlayer that = (IdlePlayer) o;
        return Objects.equals(playerUUID, that.playerUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerUUID);
    }

    // future ideas
    //    private Long fkAchievements;
//    private Long fkStatistics;
//    private Long fkPermissions;
//    private Long fkSkins;
//    private Long fkSettings;
//    private Long fkRanks;
}

