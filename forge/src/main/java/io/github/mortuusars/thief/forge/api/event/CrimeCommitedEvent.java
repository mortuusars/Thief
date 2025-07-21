package io.github.mortuusars.thief.forge.api.event;

import io.github.mortuusars.thief.world.Crime;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

/**
 * Fired when the entity (usually player) is punished for a crime.
 */
public class CrimeCommitedEvent extends Event {
    public final LivingEntity criminal;
    public final Crime crime;
    public final List<LivingEntity> witnesses;

    public CrimeCommitedEvent(LivingEntity criminal, Crime crime, List<LivingEntity> witnesses) {
        this.criminal = criminal;
        this.crime = crime;
        this.witnesses = witnesses;
    }
}