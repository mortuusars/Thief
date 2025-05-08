package io.github.mortuusars.thief.api.witness;

import io.github.mortuusars.thief.world.Crime;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public interface WitnessReactionHandler {
    /**
     * What to do when witness sees a crime.
     * @return true means 'handled' and will terminate any following handlers for the witness.
     */
    boolean handle(ServerLevel serverLevel, Crime crime, LivingEntity witness, LivingEntity criminal);
}
