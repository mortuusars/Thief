package io.github.mortuusars.thief.network.handler;

import io.github.mortuusars.thief.client.VillagerReputationTooltip;
import io.github.mortuusars.thief.network.packet.clientbound.VillagerGossipsS2CP;
import io.github.mortuusars.thief.network.packet.clientbound.VillagerReputationS2CP;

public class ClientPacketsHandler {
    public static void receiveVillagerReputation(VillagerReputationS2CP packet) {
        VillagerReputationTooltip.updateReputation(packet.villagerId(), packet.reputation());
    }

    public static void receiveVillagerGossips(VillagerGossipsS2CP packet) {
        VillagerReputationTooltip.updateGossips(packet.villagerId(), packet.majorNegative(), packet.minorNegative(),
                packet.minorPositive(), packet.majorPositive(), packet.trading());
    }
}