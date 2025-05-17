package io.github.mortuusars.thief.network.packet.clientbound;

import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.network.PacketDirection;
import io.github.mortuusars.thief.network.handler.ClientPacketsHandler;
import io.github.mortuusars.thief.network.packet.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public record VillagerGossipsS2CP(int villagerId, int majorNegative, int minorNegative, int minorPositive,
                                  int majorPositive, int trading) implements Packet {
    public static final ResourceLocation ID = Thief.resource("villager_gossips");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public FriendlyByteBuf toBuffer(FriendlyByteBuf buffer) {
        buffer.writeVarInt(villagerId);
        buffer.writeVarInt(majorNegative);
        buffer.writeVarInt(minorNegative);
        buffer.writeVarInt(minorPositive);
        buffer.writeVarInt(majorPositive);
        buffer.writeVarInt(trading);
        return buffer;
    }

    public static VillagerGossipsS2CP fromBuffer(FriendlyByteBuf buffer) {
        return new VillagerGossipsS2CP(
                buffer.readVarInt(),
                buffer.readVarInt(),
                buffer.readVarInt(),
                buffer.readVarInt(),
                buffer.readVarInt(),
                buffer.readVarInt());
    }

    @Override
    public boolean handle(PacketDirection direction, @Nullable Player player) {
        ClientPacketsHandler.receiveVillagerGossips(this);
        return true;
    }
}
