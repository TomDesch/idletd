package io.github.stealingdapenta.idletd.idleplayer.battlestats;

import static io.github.stealingdapenta.idletd.Idletd.logger;

import io.github.stealingdapenta.idletd.database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class BattleStatsRepository {

    public void saveBattleStats(BattleStats battleStats) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO BATTLE_STATS " +
                        "(PLAYERUUID, MOVEMENT_SPEED, MAX_HEALTH, REGENERATION_PER_SECOND, OVERHEAL_SHIELD_LIMIT, " +
                        "OVERHEAL_SHIELD_REGEN_PER_SECOND, KNOCKBACK_RESISTANCE, SWORD_RESISTANCE, AXE_RESISTANCE, " +
                        "MAGIC_RESISTANCE, ARROW_RESISTANCE, TRIDENT_RESISTANCE, EXPLOSION_RESISTANCE, FIRE_RESISTANCE, " +
                        "POISON_RESISTANCE, CRITICAL_HIT_RESISTANCE, BLOCK_CHANCE, ATTACK_POWER, ATTACK_RANGE, " +
                        "ATTACK_KNOCKBACK, ATTACK_SPEED, PROJECTILE_SPEED, CRITICAL_HIT_CHANCE, CRITICAL_HIT_DAMAGE_MULTIPLIER) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            statement.setString(1, battleStats.getPlayerUUID().toString());
            statement.setDouble(2, battleStats.getMovement_speed());
            statement.setDouble(3, battleStats.getMax_health());
            statement.setDouble(4, battleStats.getRegeneration_per_second());
            statement.setDouble(5, battleStats.getOverheal_shield_limit());
            statement.setDouble(6, battleStats.getOverheal_shield_regeneration_per_second());
            statement.setDouble(7, battleStats.getKnockback_resistance());
            statement.setDouble(8, battleStats.getSword_resistance());
            statement.setDouble(9, battleStats.getAxe_resistance());
            statement.setDouble(10, battleStats.getMagic_resistance());
            statement.setDouble(11, battleStats.getArrow_resistance());
            statement.setDouble(12, battleStats.getTrident_resistance());
            statement.setDouble(13, battleStats.getExplosion_resistance());
            statement.setDouble(14, battleStats.getFire_resistance());
            statement.setDouble(15, battleStats.getPoison_resistance());
            statement.setDouble(16, battleStats.getCritical_hit_resistance());
            statement.setDouble(17, battleStats.getBlock_chance());
            statement.setDouble(18, battleStats.getAttack_power());
            statement.setDouble(19, battleStats.getAttack_range());
            statement.setDouble(20, battleStats.getAttack_knockback());
            statement.setDouble(21, battleStats.getAttack_speed());
            statement.setDouble(22, battleStats.getProjectile_speed());
            statement.setDouble(23, battleStats.getCritical_hit_chance());
            statement.setDouble(24, battleStats.getCritical_hit_damage_multiplier());

            statement.execute();
        } catch (SQLException e) {
            logger.severe("Error inserting BattleStats. " + battleStats.getPlayerUUID());
            logger.warning(e.getMessage());
        }
    }

    public BattleStats getBattleStats(UUID uuid) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM BATTLE_STATS WHERE PLAYERUUID = ?")) {

            statement.setString(1, uuid.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return convertResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting BattleStats by UUID.");
            logger.warning(e.getMessage());
        }
        return null;
    }

    public void updateBattleStats(BattleStats battleStats) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE BATTLE_STATS SET " +
                        "MOVEMENT_SPEED=?, MAX_HEALTH=?, REGENERATION_PER_SECOND=?, OVERHEAL_SHIELD_LIMIT=?, " +
                        "OVERHEAL_SHIELD_REGEN_PER_SECOND=?, KNOCKBACK_RESISTANCE=?, SWORD_RESISTANCE=?, AXE_RESISTANCE=?, " +
                        "MAGIC_RESISTANCE=?, ARROW_RESISTANCE=?, TRIDENT_RESISTANCE=?, EXPLOSION_RESISTANCE=?, FIRE_RESISTANCE=?, " +
                        "POISON_RESISTANCE=?, CRITICAL_HIT_RESISTANCE=?, BLOCK_CHANCE=?, ATTACK_POWER=?, ATTACK_RANGE=?, " +
                        "ATTACK_KNOCKBACK=?, ATTACK_SPEED=?, PROJECTILE_SPEED=?, CRITICAL_HIT_CHANCE=?, " +
                        "CRITICAL_HIT_DAMAGE_MULTIPLIER=? WHERE PLAYERUUID=?")) {

            statement.setDouble(1, battleStats.getMovement_speed());
            statement.setDouble(2, battleStats.getMax_health());
            statement.setDouble(3, battleStats.getRegeneration_per_second());
            statement.setDouble(4, battleStats.getOverheal_shield_limit());
            statement.setDouble(5, battleStats.getOverheal_shield_regeneration_per_second());
            statement.setDouble(6, battleStats.getKnockback_resistance());
            statement.setDouble(7, battleStats.getSword_resistance());
            statement.setDouble(8, battleStats.getAxe_resistance());
            statement.setDouble(9, battleStats.getMagic_resistance());
            statement.setDouble(10, battleStats.getArrow_resistance());
            statement.setDouble(11, battleStats.getTrident_resistance());
            statement.setDouble(12, battleStats.getExplosion_resistance());
            statement.setDouble(13, battleStats.getFire_resistance());
            statement.setDouble(14, battleStats.getPoison_resistance());
            statement.setDouble(15, battleStats.getCritical_hit_resistance());
            statement.setDouble(16, battleStats.getBlock_chance());
            statement.setDouble(17, battleStats.getAttack_power());
            statement.setDouble(18, battleStats.getAttack_range());
            statement.setDouble(19, battleStats.getAttack_knockback());
            statement.setDouble(20, battleStats.getAttack_speed());
            statement.setDouble(21, battleStats.getProjectile_speed());
            statement.setDouble(22, battleStats.getCritical_hit_chance());
            statement.setDouble(23, battleStats.getCritical_hit_damage_multiplier());
            statement.setString(24, battleStats.getPlayerUUID().toString());

            statement.executeUpdate();

        } catch (SQLException e) {
            logger.severe("Error updating BattleStats.");
            e.printStackTrace();
        }
    }

    public void deleteBattleStats(UUID uuid) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM BATTLE_STATS WHERE PLAYERUUID = ?")) {

            statement.setString(1, uuid.toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.severe("Error deleting BattleStats.");
            logger.warning(e.getMessage());
        }
    }

    private BattleStats convertResultSet(ResultSet resultSet) throws SQLException {
        return BattleStats.builder()
                          .playerUUID(UUID.fromString(resultSet.getString("PLAYERUUID")))
                          .movement_speed(resultSet.getDouble("MOVEMENT_SPEED"))
                          .max_health(resultSet.getDouble("MAX_HEALTH"))
                          .regeneration_per_second(resultSet.getDouble("REGENERATION_PER_SECOND"))
                          .overheal_shield_limit(resultSet.getDouble("OVERHEAL_SHIELD_LIMIT"))
                          .overheal_shield_regeneration_per_second(resultSet.getDouble("OVERHEAL_SHIELD_REGEN_PER_SECOND"))
                          .knockback_resistance(resultSet.getDouble("KNOCKBACK_RESISTANCE"))
                          .sword_resistance(resultSet.getDouble("SWORD_RESISTANCE"))
                          .axe_resistance(resultSet.getDouble("AXE_RESISTANCE"))
                          .magic_resistance(resultSet.getDouble("MAGIC_RESISTANCE"))
                          .arrow_resistance(resultSet.getDouble("ARROW_RESISTANCE"))
                          .trident_resistance(resultSet.getDouble("TRIDENT_RESISTANCE"))
                          .explosion_resistance(resultSet.getDouble("EXPLOSION_RESISTANCE"))
                          .fire_resistance(resultSet.getDouble("FIRE_RESISTANCE"))
                          .poison_resistance(resultSet.getDouble("POISON_RESISTANCE"))
                          .critical_hit_resistance(resultSet.getDouble("CRITICAL_HIT_RESISTANCE"))
                          .block_chance(resultSet.getDouble("BLOCK_CHANCE"))
                          .attack_power(resultSet.getDouble("ATTACK_POWER"))
                          .attack_range(resultSet.getDouble("ATTACK_RANGE"))
                          .attack_knockback(resultSet.getDouble("ATTACK_KNOCKBACK"))
                          .attack_speed(resultSet.getDouble("ATTACK_SPEED"))
                          .projectile_speed(resultSet.getDouble("PROJECTILE_SPEED"))
                          .critical_hit_chance(resultSet.getDouble("CRITICAL_HIT_CHANCE"))
                          .critical_hit_damage_multiplier(resultSet.getDouble("CRITICAL_HIT_DAMAGE_MULTIPLIER"))
                          .build();
    }
}
