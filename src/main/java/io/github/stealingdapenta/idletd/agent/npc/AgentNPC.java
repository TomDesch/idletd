package io.github.stealingdapenta.idletd.agent.npc;

import static io.github.stealingdapenta.idletd.Idletd.logger;
import static io.github.stealingdapenta.idletd.service.utils.Time.ONE_TICK;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.skin.Skin;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AgentNPC {

    // splash dmg
    // Summon allies ability?
    // wololo ability?
    // debuff resistance
    // lifesteal?
    // debuff infliction?
    // gold drop boost
    // exp gain boost <- exp system?
    // temporary invincible ability?
    // AoE atk ability?
    // Slow ability?
    // Loot system on mobs <= more than just gold?


    private NPC npc;
    private Location location;
    private String name;

    // Calculated
    private Skin currentSkin;
    private Entity target;

    private boolean isCitizensEnabled() {
        Plugin citizens = Idletd.getInstance().getServer().getPluginManager().getPlugin("Citizens");
        return Objects.nonNull(citizens) && citizens.isEnabled();
    }

    public void spawn() {
        if (!isCitizensEnabled()) {
            Idletd.getInstance().getLogger().severe("Citizens not found or not enabled");
            return;
        }

        if (Objects.isNull(npc)) {

            this.npc = AgentNPCHandler.registry.createNPC(EntityType.PLAYER, Objects.nonNull(name) ? name : "DEFAULT NAME");
        }

        this.npc.spawn(location);

        new BukkitRunnable() {
            @Override
            public void run() {
                updateSkin();
            }
        }.runTaskLater(Idletd.getInstance(), 5 * ONE_TICK);
    }

    public void updateSkin() {
        SkinnableEntity skinnableEntity = (SkinnableEntity) npc.getEntity();

        if (Objects.isNull(skinnableEntity)) {
            logger.warning("Error updating skin for NPC " + npc.getFullName() + ". Reason: entity = null.");
            return;
        }

        SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);

        skinTrait.setSkinPersistent(currentSkin.getName(), currentSkin.getSignatureToken(), currentSkin.getDataToken());
        npc.addTrait(skinTrait);
        skinnableEntity.getSkinTracker().notifySkinChange(true);
    }

    public void updateTarget() {
        if (Objects.nonNull(getTarget()) && getTarget().isValid()) {
            this.npc.getNavigator().setTarget(getTarget(), true);
        }
    }
}
