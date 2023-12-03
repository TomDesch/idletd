package io.github.stealingdapenta.idletd.service.custommob;

import io.github.stealingdapenta.idletd.Idletd;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
public class CustomMobHandler {
    private static CustomMobHandler instance;
    private final ArrayList<MobWrapper> livingCustomMobs = new ArrayList<>();
    private final String CUSTOM_MOB_TAG = "idletd_mob";
    private final String PLAYER_TAG = "related_p";

    public static CustomMobHandler getInstance() {
        if (Objects.isNull(instance)) {
            instance = new CustomMobHandler();
        }
        return instance;
    }

    public String getPlayerTag() {
        return PLAYER_TAG;
    }

    public String getCustomMobTag() {
        return CUSTOM_MOB_TAG;
    }

    public NamespacedKey getCustomNameSpacedKey() {
        return new NamespacedKey(Idletd.getInstance(), this.getCustomMobTag());
    }

    public NamespacedKey getPlayerNameSpacedKey() {
        return new NamespacedKey(Idletd.getInstance(), this.getPlayerTag());
    }

    public void addCustomMob(MobWrapper mobWrapper) {
        livingCustomMobs.add(mobWrapper);
    }

    public List<MobWrapper> getUpdatedLivingCustomMobs() {
        removeDeadMobsFromList();
        return livingCustomMobs;
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
        return Boolean.TRUE.equals(livingEntity.getPersistentDataContainer().get(getCustomNameSpacedKey(), PersistentDataType.BOOLEAN));
    }

    public void setNewTarget(MobWrapper mobWrapper, LivingEntity target) {
        if (!(mobWrapper.getSummonedEntity() instanceof Creature entityCreature)) return;
        entityCreature.setTarget(target);
    }

    public MobWrapper spawnCustomMob(MobWrapperBuilder builder) {
        MobWrapper mobWrapper = builder.build();
        addCustomMob(mobWrapper);
        return mobWrapper;
    }
}

