package io.github.stealingdapenta.idletd.idleplayer.battlestats;

import static io.github.stealingdapenta.idletd.Idletd.LOGGER;

import io.github.stealingdapenta.idletd.database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            statement.setString(1, battleStats.getPlayerUUID().toString());
            statement.setDouble(2, battleStats.getMovementSpeed());
            statement.setDouble(3, battleStats.getMaxHealth());
            statement.setDouble(4, battleStats.getRegenerationPerSecond());
            statement.setDouble(5, battleStats.getOverhealShieldLimit());
            statement.setDouble(6, battleStats.getOverhealShieldRegenerationPerSecond());
            statement.setDouble(7, battleStats.getKnockbackResistance());
            statement.setDouble(8, battleStats.getSwordResistance());
            statement.setDouble(9, battleStats.getAxeResistance());
            statement.setDouble(10, battleStats.getMagicResistance());
            statement.setDouble(11, battleStats.getArrowResistance());
            statement.setDouble(12, battleStats.getTridentResistance());
            statement.setDouble(13, battleStats.getExplosionResistance());
            statement.setDouble(14, battleStats.getFireResistance());
            statement.setDouble(15, battleStats.getPoisonResistance());
            statement.setDouble(16, battleStats.getCriticalHitResistance());
            statement.setDouble(17, battleStats.getBlockChance());
            statement.setDouble(18, battleStats.getAttackPower());
            statement.setDouble(19, battleStats.getAttackRange());
            statement.setDouble(20, battleStats.getAttackKnockback());
            statement.setDouble(21, battleStats.getAttackSpeed());
            statement.setDouble(22, battleStats.getProjectileSpeed());
            statement.setDouble(23, battleStats.getCriticalHitChance());
            statement.setDouble(24, battleStats.getCriticalHitDamageMultiplier());

            statement.execute();
        } catch (SQLException e) {
            LOGGER.severe("Error inserting BattleStats. " + battleStats.getPlayerUUID());
            LOGGER.warning(e.getMessage());
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
            LOGGER.severe("Error getting BattleStats by UUID.");
            LOGGER.warning(e.getMessage());
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

            statement.setDouble(1, battleStats.getMovementSpeed());
            statement.setDouble(2, battleStats.getMaxHealth());
            statement.setDouble(3, battleStats.getRegenerationPerSecond());
            statement.setDouble(4, battleStats.getOverhealShieldLimit());
            statement.setDouble(5, battleStats.getOverhealShieldRegenerationPerSecond());
            statement.setDouble(6, battleStats.getKnockbackResistance());
            statement.setDouble(7, battleStats.getSwordResistance());
            statement.setDouble(8, battleStats.getAxeResistance());
            statement.setDouble(9, battleStats.getMagicResistance());
            statement.setDouble(10, battleStats.getArrowResistance());
            statement.setDouble(11, battleStats.getTridentResistance());
            statement.setDouble(12, battleStats.getExplosionResistance());
            statement.setDouble(13, battleStats.getFireResistance());
            statement.setDouble(14, battleStats.getPoisonResistance());
            statement.setDouble(15, battleStats.getCriticalHitResistance());
            statement.setDouble(16, battleStats.getBlockChance());
            statement.setDouble(17, battleStats.getAttackPower());
            statement.setDouble(18, battleStats.getAttackRange());
            statement.setDouble(19, battleStats.getAttackKnockback());
            statement.setDouble(20, battleStats.getAttackSpeed());
            statement.setDouble(21, battleStats.getProjectileSpeed());
            statement.setDouble(22, battleStats.getCriticalHitChance());
            statement.setDouble(23, battleStats.getCriticalHitDamageMultiplier());
            statement.setString(24, battleStats.getPlayerUUID().toString());

            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.severe("Error updating BattleStats.");
            e.printStackTrace();
        }
    }

    public void deleteBattleStats(UUID uuid) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM BATTLE_STATS WHERE PLAYERUUID = ?")) {

            statement.setString(1, uuid.toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.severe("Error deleting BattleStats.");
            LOGGER.warning(e.getMessage());
        }
    }

    private BattleStats convertResultSet(ResultSet resultSet) throws SQLException {
        return BattleStats.builder()
                          .playerUUID(UUID.fromString(resultSet.getString("PLAYERUUID")))
                          .movementSpeed(resultSet.getDouble("MOVEMENT_SPEED"))
                          .maxHealth(resultSet.getDouble("MAX_HEALTH"))
                          .regenerationPerSecond(resultSet.getDouble("REGENERATION_PER_SECOND"))
                          .overhealShieldLimit(resultSet.getDouble("OVERHEAL_SHIELD_LIMIT"))
                          .overhealShieldRegenerationPerSecond(resultSet.getDouble("OVERHEAL_SHIELD_REGEN_PER_SECOND"))
                          .knockbackResistance(resultSet.getDouble("KNOCKBACK_RESISTANCE"))
                          .swordResistance(resultSet.getDouble("SWORD_RESISTANCE"))
                          .axeResistance(resultSet.getDouble("AXE_RESISTANCE"))
                          .magicResistance(resultSet.getDouble("MAGIC_RESISTANCE"))
                          .arrowResistance(resultSet.getDouble("ARROW_RESISTANCE"))
                          .tridentResistance(resultSet.getDouble("TRIDENT_RESISTANCE"))
                          .explosionResistance(resultSet.getDouble("EXPLOSION_RESISTANCE"))
                          .fireResistance(resultSet.getDouble("FIRE_RESISTANCE"))
                          .poisonResistance(resultSet.getDouble("POISON_RESISTANCE"))
                          .criticalHitResistance(resultSet.getDouble("CRITICAL_HIT_RESISTANCE"))
                          .blockChance(resultSet.getDouble("BLOCK_CHANCE"))
                          .attackPower(resultSet.getDouble("ATTACK_POWER"))
                          .attackRange(resultSet.getDouble("ATTACK_RANGE"))
                          .attackKnockback(resultSet.getDouble("ATTACK_KNOCKBACK"))
                          .attackSpeed(resultSet.getDouble("ATTACK_SPEED"))
                          .projectileSpeed(resultSet.getDouble("PROJECTILE_SPEED"))
                          .criticalHitChance(resultSet.getDouble("CRITICAL_HIT_CHANCE"))
                          .criticalHitDamageMultiplier(resultSet.getDouble("CRITICAL_HIT_DAMAGE_MULTIPLIER"))
                          .build();
    }
}
