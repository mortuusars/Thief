package io.github.mortuusars.thief.world.stealth.modifier;

import io.github.mortuusars.thief.world.stealth.Stealth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class InvisibilityModifier implements Stealth.VisibilityModifier {
    @Override
    public double modify(LivingEntity entity, double value) {
        return entity.getEffect(MobEffects.INVISIBILITY) != null
                ? value * 0.2f
                : value;
    }
}
