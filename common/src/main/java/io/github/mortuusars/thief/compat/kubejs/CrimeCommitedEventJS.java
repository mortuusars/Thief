package io.github.mortuusars.thief.compat.kubejs;

import dev.latvian.mods.kubejs.entity.LivingEntityEventJS;
import io.github.mortuusars.thief.world.Crime;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

/**
 * Fired when the entity (usually player) is punished for a crime.
 */
public class CrimeCommitedEventJS extends LivingEntityEventJS {
    private final LivingEntity criminal;
    private final Crime crime;
    private final List<LivingEntity> witnesses;

    public CrimeCommitedEventJS(LivingEntity criminal, Crime crime, List<LivingEntity> witnesses) {
        this.criminal = criminal;
        this.crime = crime;
        this.witnesses = witnesses;
    }

    @Override
    public LivingEntity getEntity() {
        return criminal;
    }

    public LivingEntity getCriminal() {
        return criminal;
    }

    public Crime getCrime() {
        return crime;
    }

    public List<LivingEntity> getWitnesses() {
        return witnesses;
    }
}