package io.github.mortuusars.thief.compat.kubejs;

import dev.latvian.mods.kubejs.entity.LivingEntityEventJS;
import io.github.mortuusars.thief.world.Reputation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;

/**
 * Fired when the entity reputation with specific villager changes.
 */
public class ReputationLevelChangedEventJS extends LivingEntityEventJS {
    private final LivingEntity criminal;
    private final Villager villager;
    private final Reputation oldReputation;
    private final Reputation newReputation;

    public ReputationLevelChangedEventJS(LivingEntity criminal, Villager villager, Reputation oldReputation, Reputation newReputation) {
        this.criminal = criminal;
        this.villager = villager;
        this.oldReputation = oldReputation;
        this.newReputation = newReputation;
    }

    @Override
    public LivingEntity getEntity() {
        return criminal;
    }

    public LivingEntity getCriminal() {
        return criminal;
    }

    public Villager getVillager() {
        return villager;
    }

    public Reputation getOldReputation() {
        return oldReputation;
    }

    public Reputation getNewReputation() {
        return newReputation;
    }
}
