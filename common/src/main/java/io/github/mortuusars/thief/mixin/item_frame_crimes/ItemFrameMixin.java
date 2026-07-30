package io.github.mortuusars.thief.mixin.item_frame_crimes;

import io.github.mortuusars.thief.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin extends HangingEntity {
    @Shadow
    private boolean fixed;

    @Shadow
    public abstract ItemStack getItem();

    protected ItemFrameMixin(EntityType<? extends HangingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "dropItem(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Z)V", at = @At("HEAD"))
    private void onDropItem(ServerLevel level, Entity causedBy, boolean withFrame, CallbackInfo ci) {
        ItemStack item = getItem();
        if (!fixed && !item.isEmpty() && causedBy instanceof LivingEntity criminal && causedBy.level() instanceof ServerLevel serverLevel) {
            Config.Server.CRIME_FOR_LOOTING_ITEM_FRAME.get().getCrime().ifPresent(crime -> {
                crime.commit(serverLevel, criminal, blockPosition());
            });
        }
    }
}
