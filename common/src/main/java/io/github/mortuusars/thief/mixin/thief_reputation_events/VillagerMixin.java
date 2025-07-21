package io.github.mortuusars.thief.mixin.thief_reputation_events;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import io.github.mortuusars.thief.PlatformHelper;
import io.github.mortuusars.thief.world.Crime;
import io.github.mortuusars.thief.world.Reputation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager {
    @Shadow @Final private GossipContainer gossips;

    @Shadow public abstract GossipContainer getGossips();

    public VillagerMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "onReputationEventFrom", at = @At("HEAD"))
    private void onReputationEventPre(ReputationEventType type, Entity target, CallbackInfo ci,
                                      @Share("reputationBefore") LocalIntRef reputationBefore) {
        reputationBefore.set(getGossips().getReputation(target.getUUID(), gossip -> true));

        if (type instanceof Crime crime) {
            int major = crime.getMajorNegativeChange();
            if (major > 0) {
                gossips.add(target.getUUID(), GossipType.MAJOR_NEGATIVE, major);
            }

            int minor = crime.getMinorNegativeChange();
            if (minor > 0) {
                gossips.add(target.getUUID(), GossipType.MINOR_NEGATIVE, minor);
            }
        }
    }

    @Inject(method = "onReputationEventFrom", at = @At("RETURN"))
    private void onReputationEventPost(ReputationEventType type, Entity target, CallbackInfo ci,
                                       @Share("reputationBefore") LocalIntRef reputationBefore) {
        Reputation before = Reputation.fromValue(reputationBefore.get());
        Reputation after = Reputation.fromValue(getGossips().getReputation(target.getUUID(), gossip -> true));

        if (before != after && target instanceof LivingEntity livingEntity) {
            PlatformHelper.fireReputationLevelChangedEvent(livingEntity, (Villager)(Object)this, before, after);
        }
    }
}
