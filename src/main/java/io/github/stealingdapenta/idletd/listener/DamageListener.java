package io.github.stealingdapenta.idletd.listener;

import io.github.stealingdapenta.idletd.agent.AgentManager;
import io.github.stealingdapenta.idletd.agent.npc.AgentNPCHandler;
import io.github.stealingdapenta.idletd.custommob.CustomMobHandler;
import io.github.stealingdapenta.idletd.custommob.MobWrapper;
import io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerManager;
import io.github.stealingdapenta.idletd.idleplayer.battlestats.BattleStats;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.NPC;
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

    private final CustomMobHandler customMobHandler;
    private final AgentManager agentManager;
    private final IdlePlayerManager idlePlayerManager;


    @EventHandler(priority = EventPriority.MONITOR)
    public void calculateDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;

        Entity source = event.getDamager();
        Entity target = event.getEntity();

        if (customMobHandler.isCustomMob(source)) {
            System.out.println("Custom mob is doing a hit :)");
        }

        if (AgentNPCHandler.isNPC(target)) {
            System.out.println("Agent NPC is taking a hit :)");
        }

        if (customMobHandler.isCustomMob(source) && AgentNPCHandler.isNPC(target)) {
            handleMobHittingAgent((LivingEntity) source, (NPC) target, event);

        } else if (AgentNPCHandler.isNPC(source) && customMobHandler.isCustomMob(target)) {
            handleAgentHittingMob((NPC) source, (LivingEntity) target, event);

        } else if (source instanceof Player sourcePlayer && customMobHandler.isCustomMob(target)) {
            handlePlayerHittingMob(sourcePlayer, (LivingEntity) target, event);
        }
    }

    private void handleMobHittingAgent(LivingEntity mob, NPC agent, EntityDamageByEntityEvent event) {
        // Implement logic for calculating and applying damage todo
        System.out.printf("Mob %s is hitting agent %s for %s damage.%n", mob.getName(), agent.getName(), event.getFinalDamage());
    }

    private void handleAgentHittingMob(NPC agent, LivingEntity mob, EntityDamageByEntityEvent event) {
        // Implement logic for calculating and applying damage todo
    }

    private void handlePlayerHittingMob(Player player, LivingEntity mob, EntityDamageByEntityEvent event) {
        IdlePlayer attackingPlayer = idlePlayerManager.getOnlineIdlePlayer(player);
        BattleStats attackingStats = attackingPlayer.getFetchedBattleStats();
        MobWrapper defendingMobStats = CustomMob.createFrom(mob);
        // todo fix it all like this this is all botched and not functional
        double damage = attackingStats.getAttackPower();

        if (isMelee(event.getCause())) {
            if (isPlayerHoldingAxe(player)) {
                damage = damage - (damage * defendingMobStats.getAxe_resistance());
            } else {
                damage = damage - (damage * defendingMobStats.getSword_resistance());
            }
        }

        event.setDamage(damage);
        // Implement logic for calculating and applying damage todo

    }

    private boolean isPlayerHoldingAxe(Player player) {
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();

        return mainHandItem.getType() == Material.DIAMOND_AXE || mainHandItem.getType() == Material.GOLDEN_AXE
                || mainHandItem.getType() == Material.IRON_AXE || mainHandItem.getType() == Material.STONE_AXE
                || mainHandItem.getType() == Material.WOODEN_AXE;
    }

    private boolean isMelee(DamageCause attack) {
        return attack.equals(DamageCause.ENTITY_ATTACK) ||
                attack.equals(DamageCause.ENTITY_SWEEP_ATTACK);
    }
}
