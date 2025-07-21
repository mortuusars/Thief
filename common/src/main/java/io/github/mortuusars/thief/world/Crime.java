package io.github.mortuusars.thief.world;

import com.mojang.logging.LogUtils;
import dev.worldgen.lithostitched.worldgen.structure.DelegatingStructure;
import io.github.mortuusars.thief.Config;
import io.github.mortuusars.thief.PlatformHelper;
import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.api.witness.WitnessReaction;
import io.github.mortuusars.thief.compat.Mods;
import io.github.mortuusars.thief.compat.lithostitched.LithostitchedCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.lwjgl.system.macosx.CGEventTapCallBack;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Iterator;
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

    public String getName() {
        return name;
    }

    public int getMajorNegativeChange() {
        return switch (this) {
            case LIGHT -> Config.Common.PUNISHMENT_LIGHT_MAJOR_NEGATIVE.get();
            case MEDIUM -> Config.Common.PUNISHMENT_MEDIUM_MAJOR_NEGATIVE.get();
            case HEAVY -> Config.Common.PUNISHMENT_HEAVY_MAJOR_NEGATIVE.get();
        };
    }

    public int getMinorNegativeChange() {
        return switch (this) {
            case LIGHT -> Config.Common.PUNISHMENT_LIGHT_MINOR_NEGATIVE.get();
            case MEDIUM -> Config.Common.PUNISHMENT_MEDIUM_MINOR_NEGATIVE.get();
            case HEAVY -> Config.Common.PUNISHMENT_HEAVY_MINOR_NEGATIVE.get();
        };
    }

    public ResourceLocation getStat() {
        return switch (this) {
            case LIGHT -> Thief.Stats.CAUGHT_ON_VILLAGE_LIGHT_THEFTS;
            case MEDIUM -> Thief.Stats.CAUGHT_ON_VILLAGE_MEDIUM_THEFTS;
            case HEAVY -> Thief.Stats.CAUGHT_ON_VILLAGE_HEAVY_THEFTS;
        };
    }

    public boolean isOverGuardAttackThreshold() {
        return Config.Common.GUARD_ATTACK_THRESHOLD.get().getCrime()
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
        if (Config.Common.HERO_OF_THE_VILLAGE_CAN_STEAL.get() && criminal.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            return Outcome.NONE;
        }

        if (Config.Common.CRIME_ONLY_IN_PROTECTED_STRUCTURE.get() && !isInProtectedStructure(level, crimeTargetPosition)) {
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

        if (criminal instanceof Player player) {
            player.displayClientMessage(Component.translatable("gui.thief.crime_commited." + getName()), true);
            player.awardStat(getStat());
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

    public static PotentialCrime fromBlockStateBreaking(ServerPlayer player, BlockPos pos, BlockState state) {
        // Reverse order to select heaviest offence if added to multiple tags:
        if (state.is(Thief.Tags.Blocks.BREAK_PROTECTED_HEAVY)) return PotentialCrime.HEAVY;
        if (state.is(Thief.Tags.Blocks.BREAK_PROTECTED_MEDIUM)) return PotentialCrime.MEDIUM;
        if (state.is(Thief.Tags.Blocks.BREAK_PROTECTED_LIGHT)) return PotentialCrime.LIGHT;
        return PotentialCrime.NONE;
    }

    public static PotentialCrime fromBlockStateInteracting(ServerPlayer player, BlockPos pos, BlockState state) {
        // Reverse order to select heaviest offence if added to multiple tags:
        if (state.is(Thief.Tags.Blocks.INTERACT_PROTECTED_HEAVY)) return PotentialCrime.HEAVY;
        if (state.is(Thief.Tags.Blocks.INTERACT_PROTECTED_MEDIUM)) return PotentialCrime.MEDIUM;
        if (state.is(Thief.Tags.Blocks.INTERACT_PROTECTED_LIGHT)) return PotentialCrime.LIGHT;
        return PotentialCrime.NONE;
    }

    public static PotentialCrime fromKilling(ServerPlayer player, LivingEntity target) {
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
