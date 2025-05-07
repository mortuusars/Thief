package io.github.mortuusars.thief.event;

import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.world.Crime;
import io.github.mortuusars.thief.world.Offence;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class ServerEvents {
    public static void onBlockDestroyedByPlayer(ServerPlayer player, BlockPos pos, BlockState state) {
        Offence offence;
        if (state.is(Thief.Tags.Blocks.PROTECTED_LIGHT)) {
            offence = Offence.LIGHT;
        } else if (state.is(Thief.Tags.Blocks.PROTECTED_MODERATE)) {
            offence = Offence.MODERATE;
        } else if (state.is(Thief.Tags.Blocks.PROTECTED_HEAVY)) {
            offence = Offence.HEAVY;
        } else {
            return;
        }

        Crime.commit(player.serverLevel(), player, pos, offence);
    }
}
