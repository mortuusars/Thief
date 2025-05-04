package io.github.mortuusars.thief.world.theft;

import net.minecraft.world.entity.ai.village.ReputationEventType;

import java.util.function.Supplier;

public enum Offence {
    LIGHT(ReputationEventType.register("theft_light"), () -> 10, () -> 25),
    MODERATE(ReputationEventType.register("theft_moderate"), () -> 25, () -> 50),
    HEAVY(ReputationEventType.register("theft_heavy"), () -> 50, () -> 100);

    private final ReputationEventType reputationEventType;
    private final Supplier<Integer> tolerateThreshold;
    private final Supplier<Integer> reputationReduction;

    Offence(ReputationEventType reputationEventType, Supplier<Integer> tolerateThreshold, Supplier<Integer> reputationReduction) {
        this.reputationEventType = reputationEventType;
        this.tolerateThreshold = tolerateThreshold;
        this.reputationReduction = reputationReduction;
    }

    public ReputationEventType getReputationEventType() {
        return reputationEventType;
    }

    public int getTolerateThreshold() {
        return tolerateThreshold.get();
    }

    public int getReputationReduction() {
        return reputationReduction.get();
    }
}
