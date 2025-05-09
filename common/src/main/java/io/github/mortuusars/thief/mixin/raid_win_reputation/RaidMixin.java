package io.github.mortuusars.thief.mixin.raid_win_reputation;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.mortuusars.thief.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Raid.class)
public abstract class RaidMixin {
    @Shadow public abstract Level getLevel();
    @Shadow public abstract BlockPos getCenter();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/ResourceLocation;)V"))
    private void onTick(CallbackInfo ci, @Local LivingEntity entity) {
        List<Villager> villagers = getLevel().getEntitiesOfClass(Villager.class, new AABB(getCenter()).inflate(128));
        for (Villager villager : villagers) {
            int major = Config.Server.HERO_MAJOR_POSITIVE_INCREASE.get();
            if (major > 0) {
                villager.getGossips().add(entity.getUUID(), GossipType.MAJOR_POSITIVE, major);
            }

            int minor = Config.Server.HERO_MINOR_POSITIVE_INCREASE.get();
            if (minor > 0) {
                villager.getGossips().add(entity.getUUID(), GossipType.MINOR_POSITIVE, minor);
            }
        }
    }
}
