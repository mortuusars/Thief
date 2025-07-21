package io.github.mortuusars.thief.fabric.api.event;

import io.github.mortuusars.thief.world.Crime;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

/**
 * Fired when the entity (usually player) is punished for a crime.
 */
public interface CrimeCommitedCallback {
    Event<CrimeCommitedCallback> EVENT = EventFactory.createArrayBacked(CrimeCommitedCallback.class,
            (listeners) -> (criminal, crime, witnesses) -> {
                for (CrimeCommitedCallback listener : listeners) {
                    listener.crimeCommited(criminal, crime, witnesses);
                }
            });

    void crimeCommited(LivingEntity criminal, Crime crime, List<LivingEntity> witnesses);
}