package io.github.mortuusars.thief.forge.api.event;

import io.github.mortuusars.thief.world.Reputation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired when the entity reputation with specific villager changes.
 */
public class ReputationLevelChangedEvent extends Event {
    public final LivingEntity criminal;
    public final Villager villager;
    public final Reputation oldReputation;
    public final Reputation newReputation;

    public ReputationLevelChangedEvent(LivingEntity criminal, Villager villager, Reputation oldReputation, Reputation newReputation) {
        this.criminal = criminal;
        this.villager = villager;
        this.oldReputation = oldReputation;
        this.newReputation = newReputation;
    }
}