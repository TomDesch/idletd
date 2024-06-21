package io.github.stealingdapenta.idletd.listener;

import static com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client.USE_ITEM;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

public class DebugPacketEventListener implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        PacketListener.super.onPacketReceive(event);

        PacketTypeCommon packetType = event.getPacketType();
        int id = event.getPacketId();

        if (packetType.equals(USE_ITEM)) {
            //            LOGGER.info(ANSIColor.YELLOW, "Received packet with type %s and id %s . ".formatted(packetType, id));
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        PacketTypeCommon packetType = event.getPacketType();
        int id = event.getPacketId();

        //        if (packetType.equals(ENTITY_RELATIVE_MOVE)) {
        //            WrapperPlayServerEntityRelativeMove wrapper = new WrapperPlayServerEntityRelativeMove(event);
        //            LOGGER.info(ANSIColor.BLUE, "Send packet with type %s and id %s. ".formatted(packetType, id));
        //            LOGGER.info(ANSIColor.BLUE,
        //                        "entityID %s deltaX %s deltaY %s deltaZ %s onGround %s . ".formatted(wrapper.getEntityId(), wrapper.getDeltaX(), wrapper.getDeltaY(),
        //                                                                                             wrapper.getDeltaZ(), wrapper.isOnGround()));
        //        }
    }
}
