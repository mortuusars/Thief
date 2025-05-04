package io.github.mortuusars.thief.fabric.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.mortuusars.thief.event.ServerEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Shadow @Final protected ServerPlayer player;

    @Inject(method = "destroyBlock",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;destroy(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
                    shift = At.Shift.AFTER))
    private void onBlockDestroyed(BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local BlockState state) {
        ServerEvents.onBlockDestroyedByPlayer(player, pos, state);
    }
}
