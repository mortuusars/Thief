package io.github.mortuusars.thief.fabric.client.render;

import io.github.mortuusars.thief.client.VillagerReputationTooltip;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jspecify.annotations.NonNull;

public class VillagerReputationTooltipElement implements HudElement {
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        VillagerReputationTooltip.extract(graphics, deltaTracker);
    }
}
