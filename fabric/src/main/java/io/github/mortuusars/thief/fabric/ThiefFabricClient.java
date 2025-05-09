package io.github.mortuusars.thief.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.ThiefClient;
import io.github.mortuusars.thief.event.ClientEvents;
import io.github.mortuusars.thief.network.fabric.FabricS2CPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

public class ThiefFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ThiefClient.init();
        ConfigScreenFactoryRegistry.INSTANCE.register(Thief.ID, ConfigurationScreen::new);
        HudRenderCallback.EVENT.register(ClientEvents::renderGui);
        FabricS2CPacketHandler.register();
    }
}
