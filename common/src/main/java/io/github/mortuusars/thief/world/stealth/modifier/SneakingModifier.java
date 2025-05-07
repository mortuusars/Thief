package io.github.mortuusars.thief.world.stealth.modifier;

import io.github.mortuusars.thief.world.stealth.Stealth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SneakingModifier implements Stealth.VisibilityModifier {
    @Override
    public double modify(LivingEntity entity, double value) {
        return entity instanceof Player player && player.isSecondaryUseActive()
                ? value * 0.5f
                : value;
    }
}
