package io.github.mortuusars.thief.neoforge.compat.kubejs;

import dev.latvian.mods.kubejs.player.KubePlayerEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Fired when the player gifts an item to a villager
 */
public record GiftGivenEventJS(ServerPlayer player, Villager villager, ItemStack gift) implements KubePlayerEvent {
    @Override
    public Player getEntity() {
        return player;
    }
}
