package io.github.mortuusars.thief.mixin.carryon_compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.mortuusars.thief.event.ServerEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tschipp.carryon.common.carry.PickupHandler;

@Mixin(PickupHandler.class)
public abstract class PickupHandlerMixin {
    @ModifyReturnValue(method = "tryPickupEntity", at = @At("RETURN"), require = 0)
    private static boolean onTryPickupEntity(boolean pickedUp, @Local(argsOnly = true) ServerPlayer player, @Local(argsOnly = true) Entity entity) {
        if (pickedUp && entity instanceof LivingEntity target) {
            ServerEvents.onEntityPickedUp(player, target);
        }
        return pickedUp;
    }
}
