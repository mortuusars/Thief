package io.github.mortuusars.thief.mixin.fix_bulk_trade_reputation;

import io.github.mortuusars.thief.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager {
    @Shadow private @Nullable Player lastTradedPlayer;

    public VillagerMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "rewardTradeXp", at = @At("HEAD"))
    private void onRewardTradeXp(MerchantOffer offer, CallbackInfo ci) {
        if (Config.Server.FIX_SHIFT_CLICK_TRADE_REPUTATION.get()
                && lastTradedPlayer != null && level() instanceof ServerLevel serverLevel) {
            serverLevel.onReputationEvent(ReputationEventType.TRADE, this.lastTradedPlayer, (Villager)(Object)this);
            level().broadcastEntityEvent(this, EntityEvent.VILLAGER_HAPPY);
            lastTradedPlayer = null;
        }
    }
}
