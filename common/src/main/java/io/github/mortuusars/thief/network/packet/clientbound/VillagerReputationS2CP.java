package io.github.mortuusars.thief.network.packet.clientbound;

import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.network.PacketDirection;
import io.github.mortuusars.thief.network.handler.ClientPacketsHandler;
import io.github.mortuusars.thief.network.packet.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public record VillagerReputationS2CP(int villagerId, int reputation) implements Packet {
    public static final ResourceLocation ID = Thief.resource("villager_reputation");
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public FriendlyByteBuf toBuffer(FriendlyByteBuf buffer) {
        buffer.writeVarInt(villagerId);
        buffer.writeVarInt(reputation);
        return buffer;
    }

    public static VillagerReputationS2CP fromBuffer(FriendlyByteBuf buffer) {
        return new VillagerReputationS2CP(buffer.readVarInt(), buffer.readVarInt());
    }

    @Override
    public boolean handle(PacketDirection direction, @Nullable Player player) {
        ClientPacketsHandler.receiveVillagerReputation(this);
        return true;
    }
}
