package io.github.mortuusars.thief.fabric;

import io.github.mortuusars.thief.ThiefClient;
import io.github.mortuusars.thief.event.ClientEvents;
import io.github.mortuusars.thief.network.fabric.FabricS2CPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ThiefFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ThiefClient.init();
        HudRenderCallback.EVENT.register(ClientEvents::renderGui);
        FabricS2CPacketHandler.register();
    }
}
