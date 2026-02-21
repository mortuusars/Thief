package io.github.mortuusars.thief.neoforge.event;

import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.ThiefClient;
import io.github.mortuusars.thief.event.ClientEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = Thief.ID, value = Dist.CLIENT)
public class NeoForgeClientEvents {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ThiefClient::init);
    }

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event) {
        ClientEvents.renderGui(event.getGuiGraphics(), event.getPartialTick());
    }
}