
package io.github.mortuusars.thief.fabric.api.event;

import io.github.mortuusars.thief.world.Reputation;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

/**
 * Fired when the entity reputation with specific villager changes.
 */
public interface ReputationLevelChangedCallback {
    Event<ReputationLevelChangedCallback> EVENT = EventFactory.createArrayBacked(ReputationLevelChangedCallback.class,
            (listeners) -> (criminal, villager, oldReputation, newReputation) -> {
                for (ReputationLevelChangedCallback listener : listeners) {
                    listener.giftGiven(criminal, villager, oldReputation, newReputation);
                }
            });

    void giftGiven(LivingEntity criminal, Villager villager, Reputation oldReputation, Reputation newReputation);
}