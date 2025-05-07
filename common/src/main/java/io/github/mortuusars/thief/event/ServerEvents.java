package io.github.mortuusars.thief.event;

import io.github.mortuusars.thief.Config;
import io.github.mortuusars.thief.world.Crime;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class ServerEvents {
    public static void onBlockDestroyedByPlayer(ServerPlayer player, BlockPos pos, BlockState state) {
        if (!Config.Server.CRIME_FOR_BREAKING_PROTECTED_BLOCKS.get()) return;
        Crime.fromBlockState(player, pos, state).getCrime().ifPresent(crime ->
                crime.commit(player.serverLevel(), player, pos));
    }
}
