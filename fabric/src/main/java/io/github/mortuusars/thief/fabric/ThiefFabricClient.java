package io.github.mortuusars.thief.fabric;

import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.ThiefClient;
import io.github.mortuusars.thief.fabric.client.render.VillagerReputationTooltipElement;
import io.github.mortuusars.thief.network.fabric.FabricS2CPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

public class ThiefFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ThiefClient.init();
        ConfigScreenFactoryRegistry.INSTANCE.register(Thief.ID, ConfigurationScreen::new);
        HudElementRegistry.addLast(Thief.identifier("villager_repuitation_tooltip"), new VillagerReputationTooltipElement());
        FabricS2CPacketHandler.register();
    }
}
