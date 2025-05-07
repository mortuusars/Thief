package io.github.mortuusars.thief.mixin.bed;

import com.mojang.datafixers.util.Either;
import io.github.mortuusars.thief.Config;
import io.github.mortuusars.thief.world.Witness;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "startSleepInBed", at = @At("HEAD"))
    private void onStartSleepInBed(BlockPos bedPos, CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> cir) {
        if (!(level() instanceof ServerLevel level)) return;

        Config.Server.CRIME_FOR_SLEEPING_IN_VILLAGERS_BED.get().getCrime().ifPresent(crime -> {
            List<Villager> witnesses = Witness.getWitnesses((Player) (Object) this, Villager.class);
            for (Villager witness : witnesses) {
                if (witness.getBrain().getMemory(MemoryModuleType.HOME).map(p -> p.pos().equals(bedPos)).orElse(false)) {
                    crime.commit(level, this, bedPos);
                    return;
                }
            }
        });
    }
}
