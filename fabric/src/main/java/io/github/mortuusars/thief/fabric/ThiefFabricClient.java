package io.github.mortuusars.thief.fabric;

import io.github.mortuusars.thief.ThiefClient;
import io.github.mortuusars.thief.network.fabric.FabricS2CPacketHandler;
import net.fabricmc.api.ClientModInitializer;

public class ThiefFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ThiefClient.init();
        FabricS2CPacketHandler.register();
    }
}
