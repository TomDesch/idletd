package io.github.stealingdapenta.idletd.idleplayer.battlestats;

import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor

public class BattleStatsService {

    private final BattleStatsRepository battleStatsRepository;

    public BattleStats getFor(IdlePlayer idlePlayer) {
        return getFor(idlePlayer.getPlayerUUID());
    }

    public BattleStats getFor(Player player) {
        return getFor(player.getUniqueId());
    }

    public BattleStats getFor(UUID uuid) {
        return battleStatsRepository.getBattleStats(uuid);
    }

    public void save(BattleStats battleStats) {
        battleStatsRepository.saveBattleStats(battleStats);
    }

    public void update(BattleStats battleStats) {
        battleStatsRepository.updateBattleStats(battleStats);
    }

    public BattleStats createNew(UUID uuid) {
        BattleStats battleStats = BattleStats.builder()
                                             .playerUUID(uuid)
                                             .movementSpeed(0.2)
                                             .maxHealth(10)
                                             .regenerationPerSecond(0.01)
                                             .overhealShieldLimit(0)
                                             .overhealShieldRegenerationPerSecond(0)
                                             .knockbackResistance(0)
                                             .swordResistance(0)
                                             .axeResistance(0)
                                             .magicResistance(0)
                                             .arrowResistance(0)
                                             .tridentResistance(0)
                                             .explosionResistance(0)
                                             .fireResistance(0)
                                             .poisonResistance(0)
                                             .criticalHitResistance(0)
                                             .blockChance(0)
                                             .attackPower(0.5)
                                             .attackRange(1)
                                             .attackKnockback(0)
                                             .attackSpeed(1)
                                             .projectileSpeed(1)
                                             .criticalHitChance(0)
                                             .criticalHitDamageMultiplier(1)
                                             .build();

        save(battleStats);
        return battleStats;
    }
}
