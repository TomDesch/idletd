package io.github.stealingdapenta.idletd.towerdefense;

import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Getter
@Setter
public class TowerDefenseManager {
    private static final Set<TowerDefense> activeTDGames = new HashSet<>();


    public boolean addActiveTDGame(TowerDefense towerDefense) {
        return activeTDGames.add(towerDefense);
    }

    public boolean deactivateTDGame(TowerDefense towerDefense) {
        return activeTDGames.remove(towerDefense);
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
