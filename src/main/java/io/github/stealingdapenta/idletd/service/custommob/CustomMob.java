package io.github.stealingdapenta.idletd.service.custommob;

import io.github.stealingdapenta.idletd.Idletd;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.persistence.PersistentDataType;

@Getter
public abstract class CustomMob {

    protected EntityType entityType;

    protected double GENERIC_ARMOR = 1.0; //     Armor bonus of an Entity.
    protected double GENERIC_ATTACK_DAMAGE = 1.0; //    Armor durability bonus of an Entity.
    protected double GENERIC_ATTACK_KNOCKBACK = 1.0;    //     Attack damage of an Entity.
    protected double GENERIC_ARMOR_TOUGHNESS = 1.0;    //     Attack knockback of an Entity.
    protected double GENERIC_ATTACK_SPEED = 1.0;    //     Attack speed of an Entity.
    protected double GENERIC_FLYING_SPEED = 1.0;    //     Flying speed of an Entity.
    protected double GENERIC_FOLLOW_RANGE = 1.0;    //    Range at which an Entity will follow others.
    protected double GENERIC_KNOCKBACK_RESISTANCE = 1.0;    //    Resistance of an Entity to knockback.
    protected double GENERIC_LUCK = 1.0;    //    Luck bonus of an Entity.
    protected double GENERIC_MAX_ABSORPTION = 1.0;    //    Maximum absorption of an Entity.
    protected double GENERIC_MAX_HEALTH = 1.0;    //    Maximum health of an Entity.
    protected double GENERIC_MOVEMENT_SPEED = 1.0;    //    Movement speed of an Entity.


    public Mob summon(Location location) {
        LivingEntity livingEntity = (LivingEntity) location.getWorld().spawnEntity(location, this.getEntityType()); // todo summon "this"
        livingEntity.setCanPickupItems(false);
        livingEntity.setRemoveWhenFarAway(false);

        NamespacedKey namespacedKey = new NamespacedKey(Idletd.getInstance(), this.customMobHandler.getCUSTOM_MOB_TAG());
        livingEntity.getPersistentDataContainer().set(namespacedKey, PersistentDataType.BOOLEAN, true);

        return this;

    }

    private void setTags(LivingEntity livingEntity) {
        livingEntity.getPersistentDataContainer().set();
    }

    private void setTag(LivingEntity livingEntity,)
}
