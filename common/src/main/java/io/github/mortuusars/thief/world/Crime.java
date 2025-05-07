package io.github.mortuusars.thief.world;

import io.github.mortuusars.thief.Thief;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.List;

public class Crime {
    public static Outcome commit(ServerLevel level, LivingEntity criminal, BlockPos crimeTargetPosition, Offence offence) {
        if (!isInProtectedStructure(level, crimeTargetPosition)) {
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

        if (averageReputation.ignores(offence)) {
            return Outcome.NONE;
        }

        for (LivingEntity witness : witnesses) {
            if (witness instanceof Villager villager) {
                villager.setUnhappy();
                level.onReputationEvent(offence, criminal, villager);
                level.broadcastEntityEvent(villager, EntityEvent.VILLAGER_ANGRY);
            }
        }

        if (criminal instanceof Player player) {
            player.displayClientMessage(Component.translatable("gui.thief.crime_commited." + offence), true);
        }

        return new Outcome(true, witnesses);
    }

    // --

    public static boolean isInProtectedStructure(ServerLevel level, BlockPos pos) {
        return level.structureManager().getStructureWithPieceAt(pos, Thief.Tags.Structures.PROTECTED).isValid();
    }

    public record Outcome(boolean punished, List<LivingEntity> witnesses) {
        public static final Outcome NONE = new Outcome(false, Collections.emptyList());
    }
}
