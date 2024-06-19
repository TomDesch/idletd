package io.github.stealingdapenta.idletd.agent.mainagent;

import static io.github.stealingdapenta.idletd.Idletd.LOGGER;
import static io.github.stealingdapenta.idletd.database.DatabaseManager.getDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainAgentStatsRepository {

    public void saveMainAgentStats(MainAgentStats mainAgentStats) {
        try (Connection connection = getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO MAIN_AGENT_STATS " +
                        "(AGENT_ID, MAX_HEALTH, REGENERATION_PER_SECOND, OVERHEAL_SHIELD_LIMIT, " +
                        "OVERHEAL_SHIELD_REGEN_PER_SECOND, SWORD_RESISTANCE, AXE_RESISTANCE, " +
                        "MAGIC_RESISTANCE, ARROW_RESISTANCE, TRIDENT_RESISTANCE, EXPLOSION_RESISTANCE, FIRE_RESISTANCE, " +
                        "POISON_RESISTANCE, CRITICAL_HIT_RESISTANCE, BLOCK_CHANCE, ATTACK_POWER, ATTACK_RANGE, " +
                        "ATTACK_KNOCKBACK, ATTACK_SPEED, PROJECTILE_SPEED, CRITICAL_HIT_CHANCE, CRITICAL_HIT_DAMAGE_MULTIPLIER) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            statement.setLong(1, mainAgentStats.getAgentId());
            statement.setDouble(2, mainAgentStats.getMaxHealth());
            statement.setDouble(3, mainAgentStats.getRegenerationPerSecond());
            statement.setDouble(4, mainAgentStats.getOverhealShieldLimit());
            statement.setDouble(5, mainAgentStats.getOverhealShieldRegenerationPerSecond());
            statement.setDouble(6, mainAgentStats.getSwordResistance());
            statement.setDouble(7, mainAgentStats.getAxeResistance());
            statement.setDouble(8, mainAgentStats.getMagicResistance());
            statement.setDouble(9, mainAgentStats.getArrowResistance());
            statement.setDouble(10, mainAgentStats.getTridentResistance());
            statement.setDouble(11, mainAgentStats.getExplosionResistance());
            statement.setDouble(12, mainAgentStats.getFireResistance());
            statement.setDouble(13, mainAgentStats.getPoisonResistance());
            statement.setDouble(14, mainAgentStats.getCriticalHitResistance());
            statement.setDouble(15, mainAgentStats.getBlockChance());
            statement.setDouble(16, mainAgentStats.getAttackPower());
            statement.setDouble(17, mainAgentStats.getAttackRange());
            statement.setDouble(18, mainAgentStats.getAttackKnockback());
            statement.setDouble(19, mainAgentStats.getAttackSpeed());
            statement.setDouble(20, mainAgentStats.getProjectileSpeed());
            statement.setDouble(21, mainAgentStats.getCriticalHitChance());
            statement.setDouble(22, mainAgentStats.getCriticalHitDamageMultiplier());

            statement.execute();
        } catch (SQLException e) {
            LOGGER.severe("Error inserting MainAgentStats. Agent ID: " + mainAgentStats.getAgentId());
            LOGGER.warning(e.getMessage());
        }
    }

    public MainAgentStats getMainAgentStats(long agentId) {
        try (Connection connection = getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM MAIN_AGENT_STATS WHERE AGENT_ID = ?")) {

            statement.setLong(1, agentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return convertResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error getting MainAgentStats by Agent ID.");
            LOGGER.warning(e.getMessage());
        }
        return null;
    }

    public void updateMainAgentStats(MainAgentStats mainAgentStats) {
        try (Connection connection = getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE MAIN_AGENT_STATS SET " +
                        "MAX_HEALTH=?, REGENERATION_PER_SECOND=?, OVERHEAL_SHIELD_LIMIT=?, " +
                        "OVERHEAL_SHIELD_REGEN_PER_SECOND=?, SWORD_RESISTANCE=?, AXE_RESISTANCE=?, " +
                        "MAGIC_RESISTANCE=?, ARROW_RESISTANCE=?, TRIDENT_RESISTANCE=?, EXPLOSION_RESISTANCE=?, FIRE_RESISTANCE=?, " +
                        "POISON_RESISTANCE=?, CRITICAL_HIT_RESISTANCE=?, BLOCK_CHANCE=?, ATTACK_POWER=?, ATTACK_RANGE=?, " +
                        "ATTACK_KNOCKBACK=?, ATTACK_SPEED=?, PROJECTILE_SPEED=?, CRITICAL_HIT_CHANCE=?, " +
                        "CRITICAL_HIT_DAMAGE_MULTIPLIER=? WHERE AGENT_ID=?")) {

            statement.setDouble(1, mainAgentStats.getMaxHealth());
            statement.setDouble(2, mainAgentStats.getRegenerationPerSecond());
            statement.setDouble(3, mainAgentStats.getOverhealShieldLimit());
            statement.setDouble(4, mainAgentStats.getOverhealShieldRegenerationPerSecond());
            statement.setDouble(5, mainAgentStats.getSwordResistance());
            statement.setDouble(6, mainAgentStats.getAxeResistance());
            statement.setDouble(7, mainAgentStats.getMagicResistance());
            statement.setDouble(8, mainAgentStats.getArrowResistance());
            statement.setDouble(9, mainAgentStats.getTridentResistance());
            statement.setDouble(10, mainAgentStats.getExplosionResistance());
            statement.setDouble(11, mainAgentStats.getFireResistance());
            statement.setDouble(12, mainAgentStats.getPoisonResistance());
            statement.setDouble(13, mainAgentStats.getCriticalHitResistance());
            statement.setDouble(14, mainAgentStats.getBlockChance());
            statement.setDouble(15, mainAgentStats.getAttackPower());
            statement.setDouble(16, mainAgentStats.getAttackRange());
            statement.setDouble(17, mainAgentStats.getAttackKnockback());
            statement.setDouble(18, mainAgentStats.getAttackSpeed());
            statement.setDouble(19, mainAgentStats.getProjectileSpeed());
            statement.setDouble(20, mainAgentStats.getCriticalHitChance());
            statement.setDouble(21, mainAgentStats.getCriticalHitDamageMultiplier());
            statement.setLong(22, mainAgentStats.getAgentId());

            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.severe("Error updating MainAgentStats.");
            LOGGER.warning(e.getMessage());
        }
    }

    public void deleteMainAgentStats(long agentId) {
        try (Connection connection = getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM MAIN_AGENT_STATS WHERE AGENT_ID = ?")) {

            statement.setLong(1, agentId);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.severe("Error deleting MainAgentStats.");
            LOGGER.warning(e.getMessage());
        }
    }

    private MainAgentStats convertResultSet(ResultSet resultSet) throws SQLException {
        return MainAgentStats.builder()
                             .agentId(resultSet.getInt("AGENT_ID"))
                             .maxHealth(resultSet.getDouble("MAX_HEALTH"))
                             .regenerationPerSecond(resultSet.getDouble("REGENERATION_PER_SECOND"))
                             .overhealShieldLimit(resultSet.getDouble("OVERHEAL_SHIELD_LIMIT"))
                             .overhealShieldRegenerationPerSecond(resultSet.getDouble("OVERHEAL_SHIELD_REGEN_PER_SECOND"))
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
