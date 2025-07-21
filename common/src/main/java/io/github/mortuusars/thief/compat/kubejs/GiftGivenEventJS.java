package io.github.mortuusars.thief.compat.kubejs;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Fired when the player gifts an item to a villager
 */
public class GiftGivenEventJS extends PlayerEventJS {
    private final ServerPlayer player;
    private final LivingEntity villager;
    private final ItemStack gift;

    public GiftGivenEventJS(ServerPlayer player, Villager villager, ItemStack gift) {
        this.player = player;
        this.villager = villager;
        this.gift = gift;
    }

    @Override
    public Player getEntity() {
        return player;
    }

    @Override
    public ServerPlayer getPlayer() {
        return player;
    }

    public LivingEntity getVillager() {
        return villager;
    }

    public ItemStack getGift() {
        return gift;
    }
}
