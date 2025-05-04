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

public record VillagerGossipsS2CP(int villagerId, int majorNegative, int minorNegative, int minorPositive,
                                  int majorPositive, int trading) implements Packet {
    public static final ResourceLocation ID = Thief.resource("villager_gossips");
    public static final Type<VillagerGossipsS2CP> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, VillagerGossipsS2CP> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, VillagerGossipsS2CP::villagerId,
            ByteBufCodecs.VAR_INT, VillagerGossipsS2CP::majorNegative,
            ByteBufCodecs.VAR_INT, VillagerGossipsS2CP::minorNegative,
            ByteBufCodecs.VAR_INT, VillagerGossipsS2CP::minorPositive,
            ByteBufCodecs.VAR_INT, VillagerGossipsS2CP::majorPositive,
            ByteBufCodecs.VAR_INT, VillagerGossipsS2CP::trading,
            VillagerGossipsS2CP::new
    );

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public boolean handle(PacketFlow direction, Player player) {
        ClientPacketsHandler.receiveVillagerGossips(this);
        return true;
    }
}
