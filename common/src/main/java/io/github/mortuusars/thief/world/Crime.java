package io.github.mortuusars.thief.world;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.thief.Config;
import io.github.mortuusars.thief.Thief;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public enum Crime implements ReputationEventType {
    LIGHT("light", Config.Server.OFFENCE_LIGHT_MAJOR_NEGATIVE, Config.Server.OFFENCE_LIGHT_MINOR_NEGATIVE),
    MODERATE("moderate", Config.Server.OFFENCE_MODERATE_MAJOR_NEGATIVE, Config.Server.OFFENCE_MODERATE_MINOR_NEGATIVE),
    HEAVY("heavy", Config.Server.OFFENCE_HEAVY_MAJOR_NEGATIVE, Config.Server.OFFENCE_HEAVY_MINOR_NEGATIVE);

    private static final Logger LOGGER = LogUtils.getLogger();

    private final String name;
    private final Supplier<Integer> majorNegativeChange;
    private final Supplier<Integer> minorNegativeChange;

    Crime(String name, Supplier<Integer> majorNegativeChange, Supplier<Integer> minorNegativeChange) {
        this.name = name;
        this.majorNegativeChange = majorNegativeChange;
        this.minorNegativeChange = minorNegativeChange;
    }

    public static boolean isInProtectedStructure(ServerLevel level, BlockPos pos) {
        return level.structureManager().getStructureWithPieceAt(pos, Thief.Tags.Structures.PROTECTED).isValid();
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

    // --

    public Outcome commit(ServerLevel level, LivingEntity criminal, BlockPos crimeTargetPosition) {
        if (criminal.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) { //TODO: config
            return Outcome.NONE;
        }

        if (!isInProtectedStructure(level, crimeTargetPosition)) { //TODO: config
            return Outcome.NONE;
        }

        List<LivingEntity> witnesses = Witness.getWitnesses(criminal);
        if (witnesses.isEmpty()) {
            return Outcome.NONE;
        }

        List<Villager> villagers = witnesses.stream().filter(entity -> entity instanceof Villager)
                .map(entity -> (Villager) entity)
                .toList();

        Reputation averageReputation = Reputation.averageFromCrowd(villagers, criminal);

        if (averageReputation.ignores(this)) {
            return Outcome.NONE;
        }

        for (LivingEntity witness : witnesses) {
            if (witness instanceof Villager villager) {
                villager.setUnhappy();
                level.onReputationEvent(this, criminal, villager);
                level.broadcastEntityEvent(villager, EntityEvent.VILLAGER_ANGRY);
            }
        }

        if (criminal instanceof Player player) {
            player.displayClientMessage(Component.translatable("gui.thief.crime_commited." + getName()), true);
        }

        if (villagers.size() != witnesses.size()) {
            LOGGER.debug("{} with average reputation '{}', has commited a {} crime and was seen by {} witnesses, {} of them were villagers.",
                    criminal.getName(), averageReputation.getName(), getName(), witnesses.size(), villagers.size());
        } else {
            LOGGER.debug("{} with average reputation '{}', has commited a {} crime and was seen by {} witnesses.",
                    criminal.getName(), averageReputation.getName(), getName(), witnesses.size());
        }

        return new Outcome(true, witnesses);
    }

    // --

    public static PotentialCrime fromBlockState(ServerPlayer player, BlockPos pos, BlockState state) {
        if (state.is(Thief.Tags.Blocks.PROTECTED_LIGHT)) return PotentialCrime.LIGHT;
        if (state.is(Thief.Tags.Blocks.PROTECTED_MODERATE)) return PotentialCrime.MODERATE;
        if (state.is(Thief.Tags.Blocks.PROTECTED_HEAVY)) return PotentialCrime.HEAVY;
        return PotentialCrime.NONE;
    }

    // --

    public record Outcome(boolean punished, List<LivingEntity> witnesses) {
        public static final Outcome NONE = new Outcome(false, Collections.emptyList());
    }
}
