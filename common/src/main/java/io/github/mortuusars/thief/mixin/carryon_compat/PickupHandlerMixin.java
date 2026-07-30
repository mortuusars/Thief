package io.github.mortuusars.thief.mixin.carryon_compat;

// CarryOn is not on 26.1+ yet
//@Mixin(PickupHandler.class)
public abstract class PickupHandlerMixin {
    /*@ModifyReturnValue(method = "tryPickupEntity", at = @At("RETURN"), require = 0)
    private static boolean onTryPickupEntity(boolean pickedUp, @Local(argsOnly = true) ServerPlayer player, @Local(argsOnly = true) Entity entity) {
        if (pickedUp && entity instanceof LivingEntity target) {
            ServerEvents.onEntityPickedUp(player, target);
        }
        return pickedUp;
    }*/
}
