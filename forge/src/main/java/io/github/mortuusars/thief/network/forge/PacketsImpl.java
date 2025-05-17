package io.github.mortuusars.thief.network.forge;

import io.github.mortuusars.thief.network.PacketDirection;
import io.github.mortuusars.thief.network.packet.Packet;
import io.github.mortuusars.thief.network.packet.clientbound.VillagerGossipsS2CP;
import io.github.mortuusars.thief.network.packet.clientbound.VillagerReputationS2CP;
import io.github.mortuusars.thief.network.packet.serverbound.QueryVillagerReputationC2SP;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class PacketsImpl {
    private static final String PROTOCOL_VERSION = "1";
    private static int id = 0;

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("thief:packets"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    public static void register() {
        CHANNEL.messageBuilder(VillagerGossipsS2CP.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(VillagerGossipsS2CP::toBuffer)
                .decoder(VillagerGossipsS2CP::fromBuffer)
                .consumerMainThread(PacketsImpl::handlePacket)
                .add();
        CHANNEL.messageBuilder(VillagerReputationS2CP.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(VillagerReputationS2CP::toBuffer)
                .decoder(VillagerReputationS2CP::fromBuffer)
                .consumerMainThread(PacketsImpl::handlePacket)
                .add();

        CHANNEL.messageBuilder(QueryVillagerReputationC2SP.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(QueryVillagerReputationC2SP::toBuffer)
                .decoder(QueryVillagerReputationC2SP::fromBuffer)
                .consumerMainThread(PacketsImpl::handlePacket)
                .add();
    }

    public static void sendToServer(Packet packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToClient(Packet packet, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static void sendToAllClients(Packet packet) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }

    public static void sendToPlayersTrackingEntity(Entity entity, Packet packet) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), packet);
    }

    private static <T extends Packet> void handlePacket(T packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        packet.handle(direction(context.getDirection()), context.getSender());
    }

    private static PacketDirection direction(NetworkDirection direction) {
        if (direction == NetworkDirection.PLAY_TO_SERVER)
            return PacketDirection.TO_SERVER;
        else if (direction == NetworkDirection.PLAY_TO_CLIENT)
            return PacketDirection.TO_CLIENT;
        else
            throw new IllegalStateException("Can only convert direction for Client/Server, not others.");
    }
}