package io.github.mortuusars.thief.network.packet.serverbound;

import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.network.PacketDirection;
import io.github.mortuusars.thief.network.Packets;
import io.github.mortuusars.thief.network.packet.Packet;
import io.github.mortuusars.thief.network.packet.clientbound.VillagerGossipsS2CP;
import io.github.mortuusars.thief.network.packet.clientbound.VillagerReputationS2CP;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public record QueryVillagerReputationC2SP(int villagerId, boolean withGossips) implements Packet {
    public static final ResourceLocation ID = Thief.resource("query_villager_reputation");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public FriendlyByteBuf toBuffer(FriendlyByteBuf buffer) {
        buffer.writeVarInt(villagerId);
        buffer.writeBoolean(withGossips);
        return buffer;
    }

    public static QueryVillagerReputationC2SP fromBuffer(FriendlyByteBuf buffer) {
        return new QueryVillagerReputationC2SP(buffer.readVarInt(), buffer.readBoolean());
    }

    @Override
    public boolean handle(PacketDirection direction, @Nullable Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            Thief.LOGGER.error("Cannot handle {} packet: player is not ServerPlayer.", ID);
            return false;
        }

        @Nullable Entity entity = serverPlayer.serverLevel().getEntity(villagerId);
        if (entity instanceof Villager villager) {
            Packets.sendToClient(new VillagerReputationS2CP(entity.getId(), villager.getPlayerReputation(player)), serverPlayer);
            if (withGossips) {
                Packets.sendToClient(new VillagerGossipsS2CP(entity.getId(),
                        villager.getGossips().getReputation(player.getUUID(), type -> type == GossipType.MAJOR_NEGATIVE),
                        villager.getGossips().getReputation(player.getUUID(), type -> type == GossipType.MINOR_NEGATIVE),
                        villager.getGossips().getReputation(player.getUUID(), type -> type == GossipType.MINOR_POSITIVE),
                        villager.getGossips().getReputation(player.getUUID(), type -> type == GossipType.MAJOR_POSITIVE),
                        villager.getGossips().getReputation(player.getUUID(), type -> type == GossipType.TRADING)
                ), serverPlayer);
            }
        }

        return true;
    }
}
