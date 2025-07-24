package io.github.mortuusars.thief.neoforge.compat.kubejs;

import dev.latvian.mods.kubejs.entity.KubeEntityEvent;
import io.github.mortuusars.thief.world.Reputation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;

/**
 * Fired when the entity reputation with specific villager changes.
 */
public record ReputationLevelChangedEventJS(LivingEntity criminal, Villager villager, Reputation oldReputation,
                                            Reputation newReputation) implements KubeEntityEvent {

    @Override
    public LivingEntity getEntity() {
        return criminal;
    }
}
