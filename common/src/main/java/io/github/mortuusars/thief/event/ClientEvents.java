package io.github.mortuusars.thief.event;

import io.github.mortuusars.thief.client.VillagerReputationTooltip;
import net.minecraft.client.gui.GuiGraphics;

public class ClientEvents {
    public static void renderGui(GuiGraphics guiGraphics, float partialTicks) {
        VillagerReputationTooltip.render(guiGraphics, partialTicks);
    }
}
