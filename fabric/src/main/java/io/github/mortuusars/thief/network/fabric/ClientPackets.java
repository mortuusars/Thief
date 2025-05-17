package io.github.mortuusars.thief.network.fabric;

import io.github.mortuusars.thief.network.PacketDirection;
import io.github.mortuusars.thief.network.packet.Packet;
import io.github.mortuusars.thief.network.packet.clientbound.VillagerGossipsS2CP;
import io.github.mortuusars.thief.network.packet.clientbound.VillagerReputationS2CP;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public class ClientPackets {
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(VillagerGossipsS2CP.ID, new ClientHandler(VillagerGossipsS2CP::fromBuffer));
        ClientPlayNetworking.registerGlobalReceiver(VillagerReputationS2CP.ID, new ClientHandler(VillagerReputationS2CP::fromBuffer));
    }

    public static void sendToServer(Packet packet) {
        ClientPlayNetworking.send(packet.getId(), packet.toBuffer(PacketByteBufs.create()));
    }

    private record ClientHandler(Function<FriendlyByteBuf, Packet> decodeFunction) implements ClientPlayNetworking.PlayChannelHandler {
        @Override
        public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
            Packet packet = decodeFunction.apply(buf);
            packet.handle(PacketDirection.TO_CLIENT, null);
        }
    }
}
