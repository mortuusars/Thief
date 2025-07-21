package io.github.mortuusars.thief.fabric.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

/**
 * Fired when the player gifts an item to a villager
 */
public interface GiftGivenCallback {
    Event<GiftGivenCallback> EVENT = EventFactory.createArrayBacked(GiftGivenCallback.class,
            (listeners) -> (player, target, gift) -> {
                for (GiftGivenCallback listener : listeners) {
                    listener.giftGiven(player, target, gift);
                }
            });

    void giftGiven(ServerPlayer player, Villager villager, ItemStack gift);
}