package io.github.mortuusars.thief.neoforge.event;

import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.ThiefClient;
import io.github.mortuusars.thief.event.ClientEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class NeoForgeClientEvents {
    @EventBusSubscriber(modid = Thief.ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBus {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(ThiefClient::init);
        }
    }

    @EventBusSubscriber(modid = Thief.ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class GameBus {
        @SubscribeEvent
        public static void onRenderGuiPost(RenderGuiEvent.Post event) {
            ClientEvents.renderGui(event.getGuiGraphics(), event.getPartialTick());
        }
    }
}