package io.github.mortuusars.thief.mixin.guard_attacks_advancement;

import io.github.mortuusars.thief.Thief;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefendVillageTargetGoal.class)
public class DefendVillageTargetGoalMixin {
    @Final
    @Shadow @Nullable private IronGolem golem;
    @Shadow @Nullable private LivingEntity potentialTarget;

    @Inject(method = "start", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/target/TargetGoal;start()V"))
    private void onStart(CallbackInfo ci) {
        if (potentialTarget instanceof ServerPlayer player) {
            Thief.CriteriaTriggers.GUARD_ATTACKS_CRIMINAL.trigger(player, golem);
        }
    }
}
