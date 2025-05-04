package io.github.mortuusars.thief.network.packet.serverbound;

import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.network.Packets;
import io.github.mortuusars.thief.network.packet.Packet;
import io.github.mortuusars.thief.network.packet.clientbound.VillagerGossipsS2CP;
import io.github.mortuusars.thief.network.packet.clientbound.VillagerReputationS2CP;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record QueryVillagerReputationC2SP(int villagerId, boolean withGossips) implements Packet {
    public static final ResourceLocation ID = Thief.resource("query_villager_reputation");
    public static final CustomPacketPayload.Type<QueryVillagerReputationC2SP> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, QueryVillagerReputationC2SP> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, QueryVillagerReputationC2SP::villagerId,
            ByteBufCodecs.BOOL, QueryVillagerReputationC2SP::withGossips,
            QueryVillagerReputationC2SP::new
    );

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public boolean handle(PacketFlow direction, Player player) {
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
