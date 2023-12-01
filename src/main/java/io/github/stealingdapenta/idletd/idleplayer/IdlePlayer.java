package io.github.stealingdapenta.idletd.idleplayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class IdlePlayer {

    private int id;
    private String playerUUID;
    private double balance;
    private Long fkPlot;

    public UUID getPlayerUUIDAsUUID() {
        return UUID.fromString(playerUUID);
    }

    //    private Long fkAchievements;
//    private Long fkStatistics;
//    private Long fkPermissions;
//    private Long fkSkins;
//    private Long fkSettings;
//    private Long fkRanks;
}

