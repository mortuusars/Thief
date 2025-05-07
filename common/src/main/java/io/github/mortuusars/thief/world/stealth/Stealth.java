package io.github.mortuusars.thief.world.stealth;

import io.github.mortuusars.thief.world.stealth.modifier.DarknessModifier;
import io.github.mortuusars.thief.world.stealth.modifier.InvisibilityModifier;
import io.github.mortuusars.thief.world.stealth.modifier.SneakingModifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class Stealth {
    private static final List<VisibilityModifier> visibilityModifiers;

    static {
        visibilityModifiers = new ArrayList<>();
        addModifier(new InvisibilityModifier());
        addModifier(new SneakingModifier());
        addModifier(new DarknessModifier());
    }

    public static List<VisibilityModifier> getVisibilityModifiers() {
        return visibilityModifiers;
    }

    public static void addModifier(VisibilityModifier modifier) {
        visibilityModifiers.add(modifier);
    }

    // --

    public static double getVisibility(LivingEntity criminal) {
        double value = 1f;
        for (VisibilityModifier modifier : visibilityModifiers) {
            value = modifier.modify(criminal, value);
        }
        return Mth.clamp(value, 0.0, 1.0);
    }

    // --

    public interface VisibilityModifier {
        double modify(LivingEntity entity, double value);
    }
}
