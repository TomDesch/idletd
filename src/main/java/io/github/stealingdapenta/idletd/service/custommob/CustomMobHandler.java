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

    public static CustomMobHandler getInstance() {
        if (Objects.isNull(instance)) {
            instance = new CustomMobHandler();
        }
        return instance;
    }

    public String getCUSTOM_MOB_TAG() {
        return this.CUSTOM_MOB_TAG;
    }

    public void addCustomMob(MobWrapper mobWrapper) {
        this.livingCustomMobs.add(mobWrapper);
    }

    public List<MobWrapper> getLivingCustomMobs() {
        return livingCustomMobs;
    }

    public boolean isCustomMob(MobWrapper mobWrapper) {
        return Boolean.TRUE.equals(mobWrapper.getEntity().getPersistentDataContainer().get(new NamespacedKey(Idletd.getInstance(), this.getCUSTOM_MOB_TAG()), PersistentDataType.BOOLEAN));
    }

    public void setNewTarget(MobWrapper mobWrapper, LivingEntity target) {
        if (!(mobWrapper.getEntity() instanceof Creature entityCreature)) return;
        entityCreature.setTarget(target);
    }

    // Method to create a new custom mob during a wave
    public MobWrapper spawnCustomMob(MobWrapperBuilder builder) {
        MobWrapper mobWrapper = new MobWrapper(builder);
        addCustomMob(mobWrapper);
        return mobWrapper;
    }
}

