package io.github.stealingdapenta.idletd.custommob;

import static io.github.stealingdapenta.idletd.Idletd.logger;
import static io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob.getLevelNSK;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.agent.Agent;
import io.github.stealingdapenta.idletd.agent.npc.AgentNPC;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.persistence.PersistentDataType;


@RequiredArgsConstructor
@Getter
public class CustomMobHandler {

    private static final ArrayList<MobWrapper> livingCustomMobs = new ArrayList<>();
    private static final String ERROR_SETTING_TARGET = "Error setting agent as target for custom mob.";
    private static final String CUSTOM_MOB_TAG = "idletd_mob";
    private static final String PLAYER_TAG = "related_p";
    private static final String CUSTOM_NSK_TAG = "customnsktag";


    public void setTarget(Mob customMob, Agent target) {
        String uuid = getLinkedPlayerUUID(customMob);

        if (Objects.isNull(customMob) || Objects.isNull(uuid) || Objects.isNull(target)) {
            logger.warning(ERROR_SETTING_TARGET);
            return;
        }

        AgentNPC agentNPC = target.getAgentNPC();
        NPC npc = Optional.ofNullable(agentNPC)
                          .map(AgentNPC::getNpc)
                          .orElse(null);
        LivingEntity targetEntity = (LivingEntity) Optional.ofNullable(npc)
                                                           .map(NPC::getEntity)
                                                           .orElse(null);

        if (Objects.isNull(targetEntity)) {
            logger.warning(ERROR_SETTING_TARGET);
            return;
        }

        customMob.setTarget(targetEntity);
    }


    public static NamespacedKey getCustomNameSpacedKey() {
        return new NamespacedKey(Idletd.getInstance(), CUSTOM_MOB_TAG);
    }

    public static NamespacedKey getPlayerNameSpacedKey() {
        return new NamespacedKey(Idletd.getInstance(), PLAYER_TAG);
    }

    public void addCustomMob(MobWrapper mobWrapper) {
        livingCustomMobs.add(mobWrapper);
    }

    public void removeDeadMobsFromList() {
        livingCustomMobs.removeIf(mobWrapper -> {
            LivingEntity livingMob = mobWrapper.getSummonedEntity();
            if (Objects.nonNull(livingMob)) {
                return livingMob.isDead();
            }
            return true;
        });
    }

    public boolean isCustomMob(LivingEntity livingEntity) {
        return Boolean.TRUE.equals(livingEntity.getPersistentDataContainer()
                                               .get(getCustomNameSpacedKey(), PersistentDataType.BOOLEAN));
    }

    public boolean isCustomArmorStand(LivingEntity livingEntity) {
        if (!(livingEntity instanceof ArmorStand armorStand)) {
            return false;
        }
        return Boolean.TRUE.equals(armorStand.getPersistentDataContainer()
                                             .get(getCustomNamespacedKey(), PersistentDataType.BOOLEAN));
    }

    public boolean isCustomMobOrCustomArmorStand(LivingEntity livingEntity) {
        return isCustomMob(livingEntity) || isCustomArmorStand(livingEntity);
    }

    private NamespacedKey getCustomNamespacedKey() {
        return new NamespacedKey(Idletd.getInstance(), CUSTOM_NSK_TAG);
    }

    public String getLinkedPlayerUUID(LivingEntity customMob) {
        if (!isCustomMob(customMob)) {
            logger.warning("Tried to get Linked player uuid of an entity that is not a cutom mob!");
            return null;
        }
        return customMob.getPersistentDataContainer()
                        .get(getPlayerNameSpacedKey(), PersistentDataType.STRING);
    }

    public void setNewTarget(MobWrapper mobWrapper, LivingEntity target) {
        if (!(mobWrapper.getSummonedEntity() instanceof Creature entityCreature)) {
            return;
        }
        entityCreature.setTarget(target);
    }

    public MobWrapper spawnCustomMob(MobWrapper mobWrapper) {
        addCustomMob(mobWrapper);
        return mobWrapper;
    }

    public int getMobLevel(LivingEntity livingEntity) {
        Integer level = livingEntity.getPersistentDataContainer()
                                    .get(getLevelNSK(), PersistentDataType.INTEGER);
        if (Objects.nonNull(level)) {
            return level;
        } else {
            return -1;
        }
    }
}

