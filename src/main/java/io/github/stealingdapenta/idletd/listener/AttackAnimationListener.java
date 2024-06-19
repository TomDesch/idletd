package io.github.stealingdapenta.idletd.listener;

import static io.github.stealingdapenta.idletd.Idletd.LOGGER;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation.EntityAnimationType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class AttackAnimationListener implements PacketListener {

    /**
     * This method is a DEBUG method that will be removed later.
     *
     * @param player       The player to show the animation to
     * @param targetEntity the entity performing the animation
     */
    @Deprecated(forRemoval = true)
    public static void sendAttackAnimation(Player player, Entity targetEntity) {
        sendAnimation(player, targetEntity, EntityAnimationType.SWING_MAIN_ARM);
    }

    /**
     * This method is a DEBUG method that will be removed later.
     *
     * @param player              The player to show the animation to
     * @param targetEntity        the entity performing the animation
     * @param entityAnimationType The animation
     */
    @Deprecated(forRemoval = true)
    public static void sendAnimation(Player player, Entity targetEntity, EntityAnimationType entityAnimationType) {
        if (!(targetEntity instanceof LivingEntity livingEntity)) {
            LOGGER.warning("Target entity is not a LivingEntity. Cannot animate.");
            return;
        }

        try {
            WrapperPlayServerEntityAnimation wrapper = new WrapperPlayServerEntityAnimation(livingEntity.getEntityId(), entityAnimationType);
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, wrapper);

            LOGGER.info("Sent attack animation packet for entity: " + livingEntity.getType());

        } catch (Exception e) {
            LOGGER.warning("Failed to send attack animation packet: " + e.getMessage());
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        //Cross-platform user abstraction
        User user = event.getUser();
        //Whenever the player sends an entity interaction packet.
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity interactEntity = new WrapperPlayClientInteractEntity(event);
            WrapperPlayClientInteractEntity.InteractAction action = interactEntity.getAction();
            if (action == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                int entityID = interactEntity.getEntityId();
                //Create a chat component with the Adventure API
                Component message = Component.text("You attacked an entity.").hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                                                                               Component.text("Entity ID: " + entityID)
                                                                                                                        .color(NamedTextColor.GREEN)
                                                                                                                        .decorate(TextDecoration.BOLD)
                                                                                                                        .decorate(TextDecoration.ITALIC)));
                //Send it to the cross-platform user
                user.sendMessage(message);
            }
        }
    }
}
