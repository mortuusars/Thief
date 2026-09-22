package io.github.mortuusars.thief.mixin.bed_crimes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.mortuusars.thief.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractBedBlock;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractBedBlock.class)
public class BedBlockMixin {
    @SuppressWarnings("LocalMayUseName")
    @WrapOperation(method = "useWithoutItem", at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/world/level/block/AbstractBedBlock;kickVillagerOutOfBed(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"))
    private boolean onUseWithoutItem(AbstractBedBlock instance, Level level, BlockPos pos, Operation<Boolean> original, @Local(argsOnly = true) Player player) {
        if (original.call(instance, level, pos)) {
            if (level instanceof ServerLevel serverLevel) {
                Config.Server.CRIME_FOR_KICKING_VILLAGER_OUT_OF_BED.get().getCrime().ifPresent(crime -> {
                    if (!level.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping).isEmpty()) {
                        crime.commit(serverLevel, player, pos);
                    }
                });
            }

            return true;
        }

        return false;
    }
}
