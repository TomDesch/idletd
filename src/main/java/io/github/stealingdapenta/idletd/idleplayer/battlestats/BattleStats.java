package io.github.stealingdapenta.idletd.idleplayer.battlestats;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BattleStats {
    private UUID playerUUID;
    private double movement_speed; // 0.2 = default zombie speed

    private double max_health;
    private double regeneration_per_second;
    private double overheal_shield_limit;
    private double overheal_shield_regeneration_per_second;
    private double knockback_resistance;

    private double sword_resistance;
    private double axe_resistance;
    private double magic_resistance;
    private double arrow_resistance;
    private double trident_resistance;
    private double explosion_resistance;
    private double fire_resistance;
    private double poison_resistance;
    private double critical_hit_resistance;
    private double block_chance;

    private double attack_power;
    private double attack_range;
    private double attack_knockback;
    private double attack_speed;
    private double projectile_speed;
    private double critical_hit_chance;
    private double critical_hit_damage_multiplier;
}
