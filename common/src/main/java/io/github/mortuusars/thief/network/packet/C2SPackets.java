package io.github.mortuusars.thief.network.packet;

import io.github.mortuusars.thief.network.packet.serverbound.QueryVillagerReputationC2SP;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public class C2SPackets {
    public static List<CustomPacketPayload.TypeAndCodec<? extends FriendlyByteBuf, ? extends CustomPacketPayload>> getDefinitions() {
        return List.of(
                new CustomPacketPayload.TypeAndCodec<>(QueryVillagerReputationC2SP.TYPE, QueryVillagerReputationC2SP.STREAM_CODEC)
        );
    }
}