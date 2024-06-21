package io.github.stealingdapenta.idletd.agent.mainagent;

import static io.github.stealingdapenta.idletd.listener.CustomArmorStandCleanUpListener.getCustomNamespacedKey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataType;

@RequiredArgsConstructor
@Getter
public class MainAgentHealthBar {
    // this is not functional todo

    private ArmorStand healthBar;

    public void updateHealthBar(MainAgent mainAgent) {
        healthBar.customName(getUpdatedHP(mainAgent));
    }

    private Location getHPLocation(MainAgent mainAgent) {
        return mainAgent.getAgentNPC().getLocation().add(0, 0.5, 0);
    }

    private TextComponent getUpdatedHP(MainAgent mainAgent) {
        MainAgentStats mainAgentStats = (MainAgentStats) mainAgent.getFetchedAgentStats();

        return Component.text("lmao ( %s / %s )".formatted(mainAgentStats.getCurrentHealth(), mainAgentStats.getMaxHealth()));
    }

    public void createHealthBar(MainAgent mainAgent) {
        healthBar = getHPLocation(mainAgent).getWorld().spawn(getHPLocation(mainAgent), ArmorStand.class, armorStand -> {
            armorStand.setVisible(false);
            armorStand.setCollidable(false);
            armorStand.setInvulnerable(true);
            armorStand.setMarker(true);
            armorStand.setGravity(false);
            armorStand.setCustomNameVisible(true);
            armorStand.getPersistentDataContainer().set(getCustomNamespacedKey(), PersistentDataType.BOOLEAN, true);
            armorStand.customName(getUpdatedHP(mainAgent));
        });
    }
}