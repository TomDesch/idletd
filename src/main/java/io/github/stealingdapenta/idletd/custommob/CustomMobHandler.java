package io.github.stealingdapenta.idletd.custommob;

import static io.github.stealingdapenta.idletd.Idletd.logger;
import static io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob.getLevelNSK;

import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.agent.Agent;
import io.github.stealingdapenta.idletd.agent.npc.AgentNPC;
import io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.persistence.PersistentDataType;


@Getter
public class CustomMobHandler {

    private static final String NOT_CUSTOM_MOB = "Tried to get the Linked player uuid of an entity that is not a custom mob!";
    private static CustomMobHandler instance = null;
    private static final String ERROR_SETTING_TARGET = "Error setting agent as target for custom mob.";
    private static final String CUSTOM_MOB_TAG = "idletd_mob";
    private static final String PLAYER_TAG = "related_p";
    private static final String CUSTOM_NSK_TAG = "customnsktag";
    private final ArrayList<CustomMobLiveDataHandle> livingCustomMobsLiveData = new ArrayList<>();

    public static CustomMobHandler getInstance() {
        if (Objects.isNull(instance)) {
            instance = new CustomMobHandler();
        }
        return instance;
    }


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

    public CustomMobLiveDataHandle findBy(CustomMob customMob) {
        return getLivingCustomMobsLiveData().stream()
                                            .filter(customMobLiveDataHandle -> customMobLiveDataHandle.getCustomMob()
                                                                                                      .equals(customMob))
                                            .findFirst()
                                            .orElse(null);
    }

    public CustomMobLiveDataHandle findBy(LivingEntity livingEntity) {
        return getLivingCustomMobsLiveData().stream()
                                            .filter(customMobLiveDataHandle -> customMobLiveDataHandle.getCustomMob()
                                                                                                      .getMob()
                                                                                                      .equals(livingEntity))
                                            .findFirst()
                                            .orElse(null);
    }

    public void addCustomMob(CustomMobLiveDataHandle customMobLiveDataHandle) {
        livingCustomMobsLiveData.add(customMobLiveDataHandle);
    }

    public boolean isCustomMob(LivingEntity livingEntity) {
        return Boolean.TRUE.equals(livingEntity.getPersistentDataContainer()
                                               .get(getCustomNameSpacedKey(), PersistentDataType.BOOLEAN));
    }

    public boolean isCustomMob(Entity entity) {
        return Boolean.TRUE.equals(entity.getPersistentDataContainer()
                                         .get(getCustomNameSpacedKey(), PersistentDataType.BOOLEAN));
    }

    public String getLinkedPlayerUUID(LivingEntity customMob) {
        if (!isCustomMob(customMob)) {
            logger.warning(NOT_CUSTOM_MOB);
            return null;
        }
        return customMob.getPersistentDataContainer()
                        .get(getPlayerNameSpacedKey(), PersistentDataType.STRING);
    }

    public static int getMobLevel(LivingEntity livingEntity) {
        Integer level = livingEntity.getPersistentDataContainer()
                                    .get(getLevelNSK(), PersistentDataType.INTEGER);
        if (Objects.nonNull(level)) {
            return level;
        } else {
            return -1;
        }
    }
}

