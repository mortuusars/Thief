package io.github.mortuusars.thief.event;

import io.github.mortuusars.thief.Config;
import io.github.mortuusars.thief.api.block_interaction.BlockInteraction;
import io.github.mortuusars.thief.world.Crime;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.BlockState;

public class ServerEvents {
    public static void onBlockDestroyedByPlayer(ServerPlayer player, BlockPos pos, BlockState state) {
        if (!Config.Server.CRIME_FOR_BREAKING_PROTECTED_BLOCKS.get()) return;
        Crime.fromBlockStateBreaking(player, pos, state).getCrime().ifPresent(crime ->
                crime.commit(player.serverLevel(), player, pos));
    }

    public static void onBlockInteract(ServerPlayer player, BlockPos pos, InteractionHand hand) {
        if (!Config.Server.CRIME_FOR_INTERACTING_WITH_PROTECTED_BLOCKS.get()) return;
        if (hand == InteractionHand.OFF_HAND) return; // Handling only main hand should be enough.

        BlockState state = player.level().getBlockState(pos);

        if (BlockInteraction.handle(player, pos, state) != InteractionResult.PASS) {
            return;
        }

        Crime.fromBlockStateInteracting(player, pos, state).getCrime().ifPresent(crime ->
                crime.commit(player.serverLevel(), player, pos));
    }
}
