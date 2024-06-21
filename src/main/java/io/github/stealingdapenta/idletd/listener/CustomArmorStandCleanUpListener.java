package io.github.stealingdapenta.idletd.listener;

import io.github.stealingdapenta.idletd.Idletd;
import java.util.Arrays;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataType;

public class CustomArmorStandCleanUpListener implements Listener {

    private static final String CUSTOM_NSK_TAG = "customnsktag";

    public static NamespacedKey getCustomNamespacedKey() {
        return new NamespacedKey(Idletd.getInstance(), CUSTOM_NSK_TAG);
    }

    /**
     * In case any armor stands get 'stuck' after a server crash for example, we'll check all entities for our custom tag and remove if needed
     *
     * @param event whenever a chunk loads
     */
    @EventHandler
    public void removeStuckArmorStands(ChunkLoadEvent event) {
        Arrays.stream(event.getChunk().getEntities()).forEach(this::killIfCustom);
    }

    private void killIfCustom(Entity entity) {
        if (hasCustomNSKTag(entity)) {
            entity.remove();
        }
    }

    private boolean hasCustomNSKTag(Entity entity) {
        return Boolean.TRUE.equals(entity.getPersistentDataContainer().get(getCustomNamespacedKey(), PersistentDataType.BOOLEAN));
    }

}
