package io.github.mortuusars.thief.mixin.dont_show_traders_to_criminals;

import io.github.mortuusars.thief.world.Reputation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.ShowTradesToPlayer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ShowTradesToPlayer.class)
public class ShowTradesToPlayerMixin {
    // Prevent showing trade offers when villager shouldn't trade with a player
    @Inject(method = "checkExtraStartConditions(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/Villager;)Z",
            at = @At("HEAD"),
            cancellable = true)
    private void onCheckExtraStartConditions(ServerLevel level, Villager owner, CallbackInfoReturnable<Boolean> cir) {
        Optional<Reputation> reputation = owner.getBrain()
                .getMemory(MemoryModuleType.INTERACTION_TARGET)
                .map(target -> Reputation.fromValue(owner, target));

        if (reputation.isPresent() && !reputation.get().canTrade()) {
            cir.setReturnValue(false);
        }
    }
}
