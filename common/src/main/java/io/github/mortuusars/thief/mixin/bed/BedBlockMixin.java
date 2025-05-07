package io.github.mortuusars.thief.mixin.bed;

import io.github.mortuusars.thief.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class BedBlockMixin {
    @Inject(method = "useWithoutItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BedBlock;kickVillagerOutOfBed(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"))
    private void onUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                  BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        Config.Server.CRIME_FOR_KICKING_VILLAGER_OUT_OF_BED.get().getCrime().ifPresent(crime -> {
            if (!level.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping).isEmpty()) {
                crime.commit(serverLevel, player, pos);
            }
        });
    }
}
