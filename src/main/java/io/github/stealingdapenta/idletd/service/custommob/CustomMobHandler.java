package io.github.stealingdapenta.idletd.service.custommob;

import io.github.stealingdapenta.idletd.Idletd;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class CustomMobHandler {
    private final ArrayList<LivingEntity> livingCustomMobs = new ArrayList<>();
    private final String CUSTOM_MOB_TAG = "custom_mob";

    public String getCUSTOM_MOB_TAG() {
        return this.CUSTOM_MOB_TAG;
    }

    public void removeDeadMobs() {
        this.livingCustomMobs.removeIf(Entity::isDead);
    }

    public void addCustomMob(LivingEntity mob) {
        this.livingCustomMobs.add(mob);
    }

    public List<LivingEntity> getLivingCustomMobs() {
        return livingCustomMobs;
    }


    public boolean isCustomMob(LivingEntity mob) {
        NamespacedKey namespacedKey = new NamespacedKey(Idletd.getInstance(), this.getCUSTOM_MOB_TAG());
        return Boolean.TRUE.equals(mob.getPersistentDataContainer().get(namespacedKey, PersistentDataType.BOOLEAN));
    }

    public void setNewTarget(LivingEntity mob, LivingEntity target) {
        if (! (mob instanceof Creature entityCreature)) return; // todo error handling
        entityCreature.setTarget(target);
    }
}
