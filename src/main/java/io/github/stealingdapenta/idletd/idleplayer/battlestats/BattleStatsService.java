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
                                             .movement_speed(0.2)
                                             .max_health(10)
                                             .regeneration_per_second(0.01)
                                             .overheal_shield_limit(0)
                                             .overheal_shield_regeneration_per_second(0)
                                             .knockback_resistance(0)
                                             .sword_resistance(0)
                                             .axe_resistance(0)
                                             .magic_resistance(0)
                                             .arrow_resistance(0)
                                             .trident_resistance(0)
                                             .explosion_resistance(0)
                                             .fire_resistance(0)
                                             .poison_resistance(0)
                                             .critical_hit_resistance(0)
                                             .block_chance(0)
                                             .attack_power(0.5)
                                             .attack_range(1)
                                             .attack_knockback(0)
                                             .attack_speed(1)
                                             .projectile_speed(1)
                                             .critical_hit_chance(0)
                                             .critical_hit_damage_multiplier(1)
                                             .build();

        save(battleStats);
        return battleStats;
    }
}
