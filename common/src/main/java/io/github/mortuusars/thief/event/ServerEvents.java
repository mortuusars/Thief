package io.github.mortuusars.thief.event;

import io.github.mortuusars.thief.Thief;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ServerEvents {
    public static void onBlockDestroyedByPlayer(ServerPlayer player, BlockPos pos, BlockState state) {
//        if (!state.is(Thief.Tags.Blocks.PROTECTED)) return;

//        List<? extends LivingEntity> villagers = player.level().getNearbyEntities(Villager.class,
//                TargetingConditions.DEFAULT, player, new AABB(player.blockPosition()).inflate(16));
//
//        for (LivingEntity entity : villagers) {
//            if (!entity.hasLineOfSight(player) && entity.distanceTo(player) > 5) return;
//            Villager villager = ((Villager) entity);
//
//            villager.setLastHurtByPlayer(player);
//            villager.getGossips().add(player.getUUID(), GossipType.MAJOR_NEGATIVE, 100);
//            player.level().broadcastEntityEvent(villager, EntityEvent.VILLAGER_ANGRY);
//        }
    }

    private static boolean isInProtectedStructure(ServerLevel level, BlockPos pos) {
        //TODO: config
        return level.structureManager().getStructureWithPieceAt(pos, Thief.Tags.Structures.PROTECTED).isValid();
    }

    private static boolean isInVillage(ServerLevel level, BlockPos pos) {
        return level.isVillage(pos);
    }
}
