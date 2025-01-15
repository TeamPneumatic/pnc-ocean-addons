package me.desht.pncoceanaddons.net;

import me.desht.pncoceanaddons.PNCOceanAddons;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PNCOceanAddons.MODID)
                .versioned(PROTOCOL_VERSION);

        registrar.playToClient(NotifySeaLevelPacket.TYPE, NotifySeaLevelPacket.STREAM_CODEC, NotifySeaLevelPacket::handle);
    }
}
