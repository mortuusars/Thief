package io.github.mortuusars.thief.mixin.accessor;

import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Couldn't get accesswidener to work. This will do until that's resolved.
 */
@Mixin(Villager.class)
public interface VillagerAccessor {
    @Invoker(value = "setUnhappy")
    void thief$setUnhappy();
}
