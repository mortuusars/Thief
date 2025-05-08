package io.github.mortuusars.thief.api.block_interaction;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows registering custom handlers for block interaction in case default Thief logic is not suitable.<br>
 * Can be used in cases where only some interactions are considered a crime, and others do not.<br>
 * Or to prevent a crime depending on certain conditions.<br><br>
 * Use {@link BlockInteraction#register} to register handlers.
 */
public class BlockInteraction {
    private static final List<CustomBlockInteractHandler> handlers = new ArrayList<>();

    public static void register(CustomBlockInteractHandler handler) {
        handlers.add(handler);
    }

    public static InteractionResult handle(ServerPlayer player, BlockPos pos, BlockState state) {
        for (CustomBlockInteractHandler handler : handlers) {
            InteractionResult result = handler.handle(player, pos, state);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.PASS;
    }
}
