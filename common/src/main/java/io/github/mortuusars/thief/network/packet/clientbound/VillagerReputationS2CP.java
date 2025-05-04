package io.github.mortuusars.thief.network.packet.clientbound;

import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.network.handler.ClientPacketsHandler;
import io.github.mortuusars.thief.network.packet.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public record VillagerReputationS2CP(int villagerId, int reputation) implements Packet {
    public static final ResourceLocation ID = Thief.resource("villager_reputation");
    public static final Type<VillagerReputationS2CP> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, VillagerReputationS2CP> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, VillagerReputationS2CP::villagerId,
            ByteBufCodecs.VAR_INT, VillagerReputationS2CP::reputation,
            VillagerReputationS2CP::new
    );

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public boolean handle(PacketFlow direction, Player player) {
        ClientPacketsHandler.receiveVillagerReputation(this);
        return true;
    }
}
