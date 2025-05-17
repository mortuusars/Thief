package io.github.mortuusars.thief.network.fabric;

import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.network.PacketDirection;
import io.github.mortuusars.thief.network.packet.Packet;
import io.github.mortuusars.thief.network.packet.serverbound.QueryVillagerReputationC2SP;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class PacketsImpl {
    @Nullable
    private static MinecraftServer server;

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(QueryVillagerReputationC2SP.ID, new ServerHandler(QueryVillagerReputationC2SP::fromBuffer));
    }

    public static void registerS2CPackets() {
        ClientPackets.registerS2CPackets();
    }

    public static void sendToServer(Packet packet) {
        ClientPackets.sendToServer(packet);
    }

    public static void sendToClient(Packet packet, ServerPlayer player) {
        ServerPlayNetworking.send(player, packet.getId(), packet.toBuffer(PacketByteBufs.create()));
    }

    public static void sendToAllClients(Packet packet) {
        if (server == null) {
            Thief.LOGGER.error("Cannot send a packet to all players. Server is not present.");
            return;
        }

        FriendlyByteBuf packetBuffer = packet.toBuffer(PacketByteBufs.create());
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ServerPlayNetworking.send(player, packet.getId(), packetBuffer);
        }
    }

    public static void sendToPlayersTrackingEntity(Entity entity, Packet packet) {
        // I haven't found alternative to forge TRACKING_ENTITY target,
        // but it should not be a big deal anyway, client will not update the entity it does not have.
        sendToAllClients(packet);
    }

    public static void onServerStarting(MinecraftServer server) {
        // Store server to access from static context:
        PacketsImpl.server = server;
    }

    public static void onServerStopped(MinecraftServer server) {
        PacketsImpl.server = null;
    }

    private record ServerHandler(Function<FriendlyByteBuf, Packet> decodeFunction) implements ServerPlayNetworking.PlayChannelHandler {
        @Override
        public void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
            Packet packet = decodeFunction.apply(buf);
            packet.handle(PacketDirection.TO_SERVER, player);
        }
    }
}
