package io.github.mortuusars.thief.world;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.thief.Config;
import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.api.witness.WitnessReaction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;

public enum Crime implements ReputationEventType {
    LIGHT("light"),
    MEDIUM("medium"),
    HEAVY("heavy");

    private static final Logger LOGGER = LogUtils.getLogger();

    private final String name;

    Crime(String name) {
        this.name = name;
    }

    public static boolean isInProtectedStructure(ServerLevel level, BlockPos pos) {
        return level.structureManager().getStructureWithPieceAt(pos, Thief.Tags.Structures.PROTECTED).isValid();
    }

    public String getName() {
        return name;
    }

    public int getMajorNegativeChange() {
        return switch (this) {
            case LIGHT -> Config.Server.PUNISHMENT_LIGHT_MAJOR_NEGATIVE.get();
            case MEDIUM -> Config.Server.PUNISHMENT_MEDIUM_MAJOR_NEGATIVE.get();
            case HEAVY -> Config.Server.PUNISHMENT_HEAVY_MAJOR_NEGATIVE.get();
        };
    }

    public int getMinorNegativeChange() {
        return switch (this) {
            case LIGHT -> Config.Server.PUNISHMENT_LIGHT_MINOR_NEGATIVE.get();
            case MEDIUM -> Config.Server.PUNISHMENT_MEDIUM_MINOR_NEGATIVE.get();
            case HEAVY -> Config.Server.PUNISHMENT_HEAVY_MINOR_NEGATIVE.get();
        };
    }

    public ResourceLocation getStat() {
        return switch (this) {
            case LIGHT -> Thief.Stats.CAUGHT_ON_VILLAGE_LIGHT_THEFTS.get();
            case MEDIUM -> Thief.Stats.CAUGHT_ON_VILLAGE_MEDIUM_THEFTS.get();
            case HEAVY -> Thief.Stats.CAUGHT_ON_VILLAGE_HEAVY_THEFTS.get();
        };
    }

    public boolean isOverGuardAttackThreshold() {
        return Config.Server.GUARD_ATTACK_THRESHOLD.get().getCrime()
                .map(crime -> this.ordinal() >= crime.ordinal())
                .orElse(false);
    }

    public boolean shouldGuardsAttack(ServerLevel level, LivingEntity witness, LivingEntity criminal) {
        return isOverGuardAttackThreshold()
                && !Reputation.averageFromCrowd(criminal, Witness.getWitnesses(criminal)).ignores(this);
    }

    @Override
    public String toString() {
        return name;
    }

    // --

    public Outcome commit(ServerLevel level, LivingEntity criminal, BlockPos crimeTargetPosition) {
        if (Config.Server.HERO_OF_THE_VILLAGE_CAN_STEAL.get() && criminal.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            return Outcome.NONE;
        }

        if (Config.Server.CRIME_ONLY_IN_PROTECTED_STRUCTURE.get() && !isInProtectedStructure(level, crimeTargetPosition)) {
            return Outcome.NONE;
        }

        List<LivingEntity> witnesses = Witness.getWitnesses(criminal);
        if (witnesses.isEmpty()) {
            return Outcome.NONE;
        }

        Reputation reputation = Reputation.averageFromCrowd(criminal, witnesses);

        if (reputation.ignores(this)) {
            return Outcome.NONE;
        }

        for (LivingEntity witness : witnesses) {
            WitnessReaction.handle(level, this, witness, criminal);
        }

        if (criminal instanceof Player player) {
            player.displayClientMessage(Component.translatable("gui.thief.crime_commited." + getName()), true);
            player.awardStat(getStat());
        }

        LOGGER.debug("{} with average reputation '{}', has commited a {} crime and was seen by {} witnesses.",
                criminal.getName(), reputation.getName(), getName(), witnesses.size());

        return new Outcome(true, witnesses);
    }

    // --

    public static PotentialCrime fromBlockStateBreaking(ServerPlayer player, BlockPos pos, BlockState state) {
        if (state.is(Thief.Tags.Blocks.BREAK_PROTECTED_LIGHT)) return PotentialCrime.LIGHT;
        if (state.is(Thief.Tags.Blocks.BREAK_PROTECTED_MEDIUM)) return PotentialCrime.MEDIUM;
        if (state.is(Thief.Tags.Blocks.BREAK_PROTECTED_HEAVY)) return PotentialCrime.HEAVY;
        return PotentialCrime.NONE;
    }

    public static PotentialCrime fromBlockStateInteracting(ServerPlayer player, BlockPos pos, BlockState state) {
        if (state.is(Thief.Tags.Blocks.INTERACT_PROTECTED_LIGHT)) return PotentialCrime.LIGHT;
        if (state.is(Thief.Tags.Blocks.INTERACT_PROTECTED_MEDIUM)) return PotentialCrime.MEDIUM;
        if (state.is(Thief.Tags.Blocks.INTERACT_PROTECTED_HEAVY)) return PotentialCrime.HEAVY;
        return PotentialCrime.NONE;
    }

    // --

    public record Outcome(boolean punished, List<LivingEntity> witnesses) {
        public static final Outcome NONE = new Outcome(false, Collections.emptyList());
    }
}
