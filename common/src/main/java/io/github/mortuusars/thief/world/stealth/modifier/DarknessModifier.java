package io.github.mortuusars.thief.world.stealth.modifier;

import io.github.mortuusars.thief.world.stealth.Stealth;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class DarknessModifier implements Stealth.VisibilityModifier {
    @Override
    public double modify(LivingEntity entity, double value) {
        int lightLevel = getLightLevelAt(entity.level(), entity.blockPosition());
        return value * Mth.map(lightLevel, 0, 15, 0.25f, 1f);
    }

    public static int getLightLevelAt(Level level, BlockPos pos) {
        if (level.isClientSide) {
            // This updates 'getSkyDarken' on the client. It'll return 0 if we don't update it.
            level.updateSkyBrightness();
        }
        int skyBrightness = level.getBrightness(LightLayer.SKY, pos);
        int blockBrightness = level.getBrightness(LightLayer.BLOCK, pos);
        return skyBrightness < 15 ?
                Math.max(blockBrightness, (int) (skyBrightness * ((15 - level.getSkyDarken()) / 15f))) :
                Math.max(blockBrightness, 15 - level.getSkyDarken());
    }
}
