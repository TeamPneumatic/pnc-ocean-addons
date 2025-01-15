package me.desht.pncoceanaddons.net;

import me.desht.pncoceanaddons.PNCOceanAddons;
import me.desht.pncoceanaddons.client.DepthUpgradeClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Received on: CLIENT<br>
 * Sent by server when players enter a level so the client knows the true sea level for the level
 *
 * @param seaLevel height of sea level
 */
public record NotifySeaLevelPacket(int seaLevel) implements CustomPacketPayload {
    static final Type<NotifySeaLevelPacket> TYPE = new Type<>(PNCOceanAddons.id("notify_sea_level"));

    static final StreamCodec<FriendlyByteBuf, NotifySeaLevelPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, NotifySeaLevelPacket::seaLevel,
            NotifySeaLevelPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(NotifySeaLevelPacket packet, IPayloadContext context) {
        DepthUpgradeClientHandler.recordSeaLevel(context.player().level(), packet.seaLevel);
    }
}
