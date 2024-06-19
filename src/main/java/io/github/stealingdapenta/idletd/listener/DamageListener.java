package io.github.stealingdapenta.idletd.listener;

import static io.github.stealingdapenta.idletd.Idletd.LOGGER;

import io.github.stealingdapenta.idletd.agent.Agent;
import io.github.stealingdapenta.idletd.agent.AgentManager;
import io.github.stealingdapenta.idletd.custommob.CustomMobHandler;
import io.github.stealingdapenta.idletd.custommob.CustomMobLiveDataHandle;
import io.github.stealingdapenta.idletd.custommob.MobWrapper;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerManager;
import io.github.stealingdapenta.idletd.idleplayer.battlestats.BattleStats;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class DamageListener implements Listener {

    // todo recalculate dmg player to custom mob
    // todo recalculate dmg custom mob to player or Agent NPC
    // todo calculate dmg done by Agent to custom mob
    // todo cancel dmg done by agent to player or agent

    private final CustomMobHandler customMobHandler = CustomMobHandler.getInstance();
    ;
    private final AgentManager agentManager;
    private final IdlePlayerManager idlePlayerManager;


    @EventHandler(priority = EventPriority.MONITOR)
    public void calculateDamage(EntityDamageByEntityEvent event) {
        Entity source = event.getDamager();
        Entity target = event.getEntity();

        if (customMobHandler.isCustomMob(source) && CitizensAPI.getNPCRegistry().isNPC(target)) {
            Agent targetAgent = agentManager.getAgentMatchingNPC(target);
            handleMobHittingAgent((LivingEntity) source, targetAgent, event);

        } else if (CitizensAPI.getNPCRegistry().isNPC(source) && customMobHandler.isCustomMob(target)) {
            handleAgentHittingMob((NPC) source, (LivingEntity) target, event);

        } else if (source instanceof Player sourcePlayer && customMobHandler.isCustomMob(target)) {
            handlePlayerHittingMob(sourcePlayer, (LivingEntity) target, event);
        }
    }

    private void handleMobHittingAgent(LivingEntity mob, Agent agent, EntityDamageByEntityEvent event) {
        // Implement logic for calculating and applying damage todo
        System.out.printf("Mob %s is hitting agent %s for %s damage.%n", mob.getName(), agent.getAgentNPC().getNpc().getName(), event.getFinalDamage());
    }

    private void handleAgentHittingMob(NPC agent, LivingEntity mob, EntityDamageByEntityEvent event) {
        // Implement logic for calculating and applying damage todo
    }

    private void handlePlayerHittingMob(Player player, LivingEntity mob, EntityDamageByEntityEvent event) {
        IdlePlayer attackingPlayer = idlePlayerManager.getOnlineIdlePlayer(player);
        BattleStats attackingStats = attackingPlayer.getFetchedBattleStats();
        CustomMobLiveDataHandle defendingMobLiveData = customMobHandler.findBy(mob); // todo make NPE proof

        if (Objects.isNull(defendingMobLiveData)) {
            LOGGER.warning("Defending mob has no defendingMobLiveData.");
            return;
        }

        MobWrapper defendingMobStats = defendingMobLiveData.getMobWrapper();
        // todo fix it all like this this is all botched and not functional
        double damage = attackingStats.getAttackPower();

        if (isMelee(event.getCause())) {
            if (isPlayerHoldingAxe(player)) {
                damage = damage - (damage * defendingMobStats.getAxeResistance());
            } else {
                damage = damage - (damage * defendingMobStats.getSwordResistance());
            }
        }

        event.setDamage(damage);
        // Implement logic for calculating and applying damage todo

    }

    private boolean isPlayerHoldingAxe(Player player) {
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();

        return mainHandItem.getType() == Material.DIAMOND_AXE || mainHandItem.getType() == Material.GOLDEN_AXE || mainHandItem.getType() == Material.IRON_AXE
                || mainHandItem.getType() == Material.STONE_AXE || mainHandItem.getType() == Material.WOODEN_AXE;
    }

    private boolean isMelee(DamageCause attack) {
        return attack.equals(DamageCause.ENTITY_ATTACK) || attack.equals(DamageCause.ENTITY_SWEEP_ATTACK);
    }
}
