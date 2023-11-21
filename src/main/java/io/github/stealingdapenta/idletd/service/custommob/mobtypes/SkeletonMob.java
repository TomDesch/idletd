package io.github.stealingdapenta.idletd.service.custommob.mobtypes;

import org.bukkit.entity.EntityType;

public class SkeletonMob extends CustomMob {

    public SkeletonMob() {
        this.entityType = EntityType.SKELETON;
        // Set specific attributes for SkeletonMob
        this.ARMOR = 1.5;
        this.ATTACK_DAMAGE = 2.5;
        // Set other attributes as needed
    }

    // Implement any additional methods specific to SkeletonMob
}