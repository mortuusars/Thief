package io.github.mortuusars.thief.world;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import io.github.mortuusars.thief.Config;
import io.github.mortuusars.thief.PlatformHelper;
import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.api.witness.WitnessReaction;
import io.github.mortuusars.thief.compat.Mods;
import io.github.mortuusars.thief.compat.lithostitched.LithostitchedCompat;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;

public enum Crime implements ReputationEventType, StringRepresentable {
    LIGHT("light"),
    MEDIUM("medium"),
    HEAVY("heavy");

    public static final Codec<Crime> CODEC = StringRepresentable.fromEnum(Crime::values);
    public static final IntFunction<Crime> BY_ID = ByIdMap.continuous(Crime::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, Crime> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Crime::ordinal);

    private static final Logger LOGGER = LogUtils.getLogger();

    private final String name;

    Crime(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public @NotNull String getSerializedName() {
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
            case LIGHT -> Thief.Stats.CAUGHT_COMMITING_LIGHT_CRIMES.get();
            case MEDIUM -> Thief.Stats.CAUGHT_COMMITING_MEDIUM_CRIMES.get();
            case HEAVY -> Thief.Stats.CAUGHT_COMMITING_HEAVY_CRIMES.get();
        };
    }

    public boolean isOverGuardAttackThreshold() {
        return Config.Server.GUARD_ATTACK_THRESHOLD.get().getCrime()
                .map(crime -> this.ordinal() >= crime.ordinal())
                .orElse(false);
    }

    public boolean shouldGuardsAttack(ServerLevel level, LivingEntity criminal) {
        return isOverGuardAttackThreshold()
                && !Reputation.averageFromCrowd(criminal, Witness.getWitnesses(criminal)).ignores(this);
    }

    @Override
    public String toString() {
        return name;
    }

    // --

    public Outcome commit(ServerLevel level, LivingEntity criminal, BlockPos crimeTargetPosition) {
        if (Config.Server.HERO_OF_THE_VILLAGE_CAN_COMMIT_CRIMES.get() && criminal.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
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

        PlatformHelper.fireCrimeCommitedEvent(criminal, this, witnesses);

        if (criminal instanceof ServerPlayer player) {
            if (Config.Server.CRIME_SHOW_MESSAGE.get()) {
                player.displayClientMessage(Component.translatable("gui.thief.crime_commited." + getName()), true);
            }
            player.awardStat(getStat());

            Thief.CriteriaTriggers.CRIME_COMMITED.get().trigger(player, this, witnesses);
        }

        LOGGER.debug("{} with average reputation '{}', has commited a {} crime and was seen by {} witnesses.",
                criminal.getName(), reputation.getName(), getName(), witnesses.size());

        return new Outcome(true, witnesses);
    }

    public static boolean isInProtectedStructure(ServerLevel level, BlockPos pos) {
        if (Mods.LITHOSTITCHED.isLoaded()) {
            return LithostitchedCompat.getStructureWithPieceAt(level, pos, Thief.Tags.Structures.PROTECTED).isValid();
        }
        return level.structureManager().getStructureWithPieceAt(pos, Thief.Tags.Structures.PROTECTED).isValid();
    }

    // --

    public static PotentialCrime fromBlockStateBreaking(Player player, BlockPos pos, BlockState state) {
        if (!Config.Server.CRIME_FOR_BREAKING_PROTECTED_BLOCKS.get()) return PotentialCrime.NONE;
        // Reverse order to select heaviest offence if added to multiple tags:
        if (state.is(Thief.Tags.Blocks.BREAK_PROTECTED_HEAVY)) return PotentialCrime.HEAVY;
        if (state.is(Thief.Tags.Blocks.BREAK_PROTECTED_MEDIUM)) return PotentialCrime.MEDIUM;
        if (state.is(Thief.Tags.Blocks.BREAK_PROTECTED_LIGHT)) return PotentialCrime.LIGHT;
        return PotentialCrime.NONE;
    }

    public static PotentialCrime fromBlockStateInteracting(Player player, BlockPos pos, BlockState state) {
        if (!Config.Server.CRIME_FOR_INTERACTING_WITH_PROTECTED_BLOCKS.get()) return PotentialCrime.NONE;
        // Reverse order to select heaviest offence if added to multiple tags:
        if (state.is(Thief.Tags.Blocks.INTERACT_PROTECTED_HEAVY)) return PotentialCrime.HEAVY;
        if (state.is(Thief.Tags.Blocks.INTERACT_PROTECTED_MEDIUM)) return PotentialCrime.MEDIUM;
        if (state.is(Thief.Tags.Blocks.INTERACT_PROTECTED_LIGHT)) return PotentialCrime.LIGHT;
        return PotentialCrime.NONE;
    }

    public static PotentialCrime fromKilling(Player player, LivingEntity target) {
        if (!Config.Server.CRIME_FOR_KILLING_PROTECTED_ENTITIES.get()) return PotentialCrime.NONE;
        // Reverse order to select heaviest offence if added to multiple tags:
        if (target.getType().is(Thief.Tags.EntityTypes.KILLING_PROTECTED_HEAVY)) return PotentialCrime.HEAVY;
        if (target.getType().is(Thief.Tags.EntityTypes.KILLING_PROTECTED_MEDIUM)) return PotentialCrime.MEDIUM;
        if (target.getType().is(Thief.Tags.EntityTypes.KILLING_PROTECTED_LIGHT)) return PotentialCrime.LIGHT;
        return PotentialCrime.NONE;
    }

    // --

    public record Outcome(boolean punished, List<LivingEntity> witnesses) {
        public static final Outcome NONE = new Outcome(false, Collections.emptyList());
    }
}
