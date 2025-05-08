package io.github.mortuusars.thief.api.block_interaction;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.BlockState;

public interface CustomBlockInteractHandler {
    /**
     * Handles block interaction. This is called for every block, not just ones tagged as #thief:interact_protected/level.
     * @return If anything other than PASS is returned, default Thief logic is cancelled, and no justice will be served.<br>
     */
    InteractionResult handle(ServerPlayer player, BlockPos pos, BlockState state);
}
