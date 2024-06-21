package io.github.stealingdapenta.idletd.custommob;

import static io.github.stealingdapenta.idletd.Idletd.LOGGER;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation.EntityAnimationType;
import io.github.stealingdapenta.idletd.custommob.mobtypes.CustomMob;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class AttackAnimationHandler {

    private static AttackAnimationHandler instance;
    // todo 3) display Hero health somewhere
    // todo 4) Calculate dealt damage & inflict it
    // todo 5) Game over when hero dead then what?
    // **> Remove wave; stop waves; restart from there : fined? / cost
    // todo 6) Make hero fight back

    public static AttackAnimationHandler getInstance() {
        if (Objects.isNull(instance)) {
            instance = new AttackAnimationHandler();
        }
        return instance;
    }

    /**
     * @param player       The player to show the animation to
     * @param targetEntity the entity performing the animation
     */
    public void sendMeleeAttackAnimation(Player player, Entity targetEntity) {
        sendAnimation(player, targetEntity, EntityAnimationType.SWING_MAIN_ARM);
    }

    public void sendMeleeAttackAnimation(Player player, CustomMob customMob) {
        sendAnimation(player, customMob.getMob(), EntityAnimationType.SWING_MAIN_ARM);
    }


    /**
     * @param player              The player to show the animation to
     * @param targetEntity        the entity performing the animation
     * @param entityAnimationType The animation
     */
    public void sendAnimation(Player player, Entity targetEntity, EntityAnimationType entityAnimationType) {
        if (!(targetEntity instanceof LivingEntity livingEntity)) {
            return;
        }

        try {
            WrapperPlayServerEntityAnimation wrapper = new WrapperPlayServerEntityAnimation(livingEntity.getEntityId(), entityAnimationType);
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, wrapper);
        } catch (Exception e) {
            LOGGER.warning("Failed to send attack animation packet: " + e.getMessage());
        }
    }
}
