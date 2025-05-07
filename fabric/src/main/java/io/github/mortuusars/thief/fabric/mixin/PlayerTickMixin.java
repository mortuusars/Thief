package io.github.mortuusars.thief.fabric.mixin;

import io.github.mortuusars.thief.event.CommonEvents;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerTickMixin {
    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        CommonEvents.onPlayerTick((Player)(Object)this);
    }
}
