package io.github.mortuusars.thief.neoforge.compat.kubejs;

import dev.latvian.mods.kubejs.entity.KubeEntityEvent;
import io.github.mortuusars.thief.world.Crime;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

/**
 * Fired when the entity (usually player) is punished for a crime.
 */
public record CrimeCommitedEventJS(LivingEntity criminal,
                                   Crime crime,
                                   List<LivingEntity> witnesses) implements KubeEntityEvent {
    @Override
    public LivingEntity getEntity() {
        return criminal;
    }
}