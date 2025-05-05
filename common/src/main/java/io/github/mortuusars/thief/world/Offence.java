package io.github.mortuusars.thief.world;

import io.github.mortuusars.thief.Config;
import net.minecraft.world.entity.ai.village.ReputationEventType;

import java.util.function.Supplier;

public enum Offence implements ReputationEventType {
    LIGHT("theft_light", Config.Server.OFFENCE_LIGHT_MAJOR_NEGATIVE, Config.Server.OFFENCE_LIGHT_MINOR_NEGATIVE),
    MODERATE("theft_moderate", Config.Server.OFFENCE_MODERATE_MAJOR_NEGATIVE, Config.Server.OFFENCE_MODERATE_MINOR_NEGATIVE),
    HEAVY("theft_heavy", Config.Server.OFFENCE_HEAVY_MAJOR_NEGATIVE, Config.Server.OFFENCE_HEAVY_MINOR_NEGATIVE);

    private final String name;
    private final Supplier<Integer> majorNegativeChange;
    private final Supplier<Integer> minorNegativeChange;

    Offence(String name, Supplier<Integer> majorNegativeChange, Supplier<Integer> minorNegativeChange) {
        this.name = name;
        this.majorNegativeChange = majorNegativeChange;
        this.minorNegativeChange = minorNegativeChange;
    }

    public String getName() {
        return name;
    }

    public int getMajorNegativeChange() {
        return majorNegativeChange.get();
    }

    public int getMinorNegativeChange() {
        return minorNegativeChange.get();
    }

    @Override
    public String toString() {
        return name;
    }
}
