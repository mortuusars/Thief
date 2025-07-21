package io.github.mortuusars.thief.forge.api.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired when the player gifts an item to a villager
 */
public class GiftGivenEvent extends Event {
    public final ServerPlayer player;
    public final Villager villager;
    public final ItemStack gift;

    public GiftGivenEvent(ServerPlayer player, Villager villager, ItemStack gift) {
        this.player = player;
        this.villager = villager;
        this.gift = gift;
    }
}