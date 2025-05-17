package io.github.mortuusars.thief.forge.event;

import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.ThiefClient;
import io.github.mortuusars.thief.event.ClientEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ForgeClientEvents {
    @Mod.EventBusSubscriber(modid = Thief.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBus {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(ThiefClient::init);
        }
    }

    @Mod.EventBusSubscriber(modid = Thief.ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class GameBus {
        @SubscribeEvent
        public static void onRenderGuiPost(RenderGuiEvent.Post event) {
            ClientEvents.renderGui(event.getGuiGraphics(), event.getPartialTick());
        }
    }
}