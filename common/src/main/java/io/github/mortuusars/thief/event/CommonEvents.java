package io.github.mortuusars.thief.event;

import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.world.Reputation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;

public class CommonEvents {
    private static long lastGiftSoundPlayedAt = -1;

    public static InteractionResult onEntityInteracted(Player player, InteractionHand hand, Entity target) {
        if (!(player instanceof ServerPlayer serverPlayer) || !(target instanceof Villager villager)) {
            return InteractionResult.PASS;
        }

        ItemStack item = player.getItemInHand(hand);
        if (canGift(serverPlayer, villager, item)) {
            item.shrink(1);
            villager.getGossips().add(player.getUUID(), GossipType.MINOR_POSITIVE, 2);
            villager.getGossips().remove(player.getUUID(), GossipType.MINOR_NEGATIVE, 5);

            // Calm down panicking villager
            if (villager.getBrain().isActive(Activity.PANIC) && villager.getPlayerReputation(player) > -60) {
                villager.getBrain().setActiveActivityIfPossible(Activity.IDLE);
            }

            player.level().broadcastEntityEvent(villager, EntityEvent.VILLAGER_HAPPY);
            if (player.level().getGameTime() - lastGiftSoundPlayedAt > 10) { // Prevent sound spam
                lastGiftSoundPlayedAt = player.level().getGameTime();
                player.level().playSound(null, villager, SoundEvents.VILLAGER_CELEBRATE, SoundSource.NEUTRAL, 1, 1);
            }
            return InteractionResult.SUCCESS;
        }

        if (Reputation.fromValue(villager.getPlayerReputation(player)).isLowerOrEqualTo(Reputation.UNWELCOME)) {
            // Prevent trading
            villager.setUnhappy();
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }
        return InteractionResult.PASS;
    }

    public static boolean canGift(ServerPlayer player, Villager villager, ItemStack item) {
        if (!item.is(Thief.Tags.Items.VILLAGER_GIFTS)) {
            return false;
        }
        int minorPositiveRep = villager.getGossips().getReputation(player.getUUID(), gossipType -> gossipType == GossipType.MINOR_POSITIVE);
        int minorNegativeRep = villager.getGossips().getReputation(player.getUUID(), gossipType -> gossipType == GossipType.MINOR_NEGATIVE);
        return minorPositiveRep < GossipType.MINOR_POSITIVE.max || minorNegativeRep < 0;
    }
}
