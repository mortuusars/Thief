package io.github.mortuusars.thief.event;

import com.mojang.brigadier.CommandDispatcher;
import io.github.mortuusars.thief.Config;
import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.command.ThiefCommand;
import io.github.mortuusars.thief.world.Reputation;
import io.github.mortuusars.thief.world.Witness;
import io.github.mortuusars.thief.world.stealth.Stealth;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CommonEvents {
    private static long lastGiftSoundPlayedAt = -1;

    public static InteractionResult onEntityInteracted(Player player, InteractionHand hand, Entity target) {
        if (hand != InteractionHand.MAIN_HAND || !(player instanceof ServerPlayer serverPlayer) || !(target instanceof Villager villager)) {
            return InteractionResult.PASS;
        }

        ItemStack item = player.getItemInHand(hand);
        if ((!Config.Server.REQUIRES_SNEAK.get() || player.isSecondaryUseActive()) && canGift(serverPlayer, villager, item)) {
            ItemStack gift = item.split(1);

            villager.getGossips().add(player.getUUID(), GossipType.MINOR_POSITIVE,
                    Config.Server.GIFTS_MINOR_POSITIVE_INCREASE.get());
            villager.getGossips().remove(player.getUUID(), GossipType.MINOR_NEGATIVE,
                    Config.Server.GIFTS_MINOR_NEGATIVE_REDUCTION.get());

            // Calm down panicking villager
            if (villager.getBrain().isActive(Activity.PANIC) && villager.getPlayerReputation(player) > -60) {
                villager.getBrain().setActiveActivityIfPossible(Activity.IDLE);
            }

            player.level().broadcastEntityEvent(villager, EntityEvent.VILLAGER_HAPPY);
            if (player.level().getGameTime() - lastGiftSoundPlayedAt > 10) { // Prevent sound spam
                player.level().playSound(null, villager, SoundEvents.VILLAGER_CELEBRATE, SoundSource.NEUTRAL, 1, 1);
                lastGiftSoundPlayedAt = player.level().getGameTime();
            }

            Thief.CriteriaTriggers.VILLAGER_GIFT.get().trigger(serverPlayer, villager, gift);

            return InteractionResult.SUCCESS;
        }

        if (!Reputation.fromValue(villager, player).canTrade()) {
            // Prevent trading
            villager.setUnhappy();
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }

        return InteractionResult.PASS;
    }

    public static boolean canGift(ServerPlayer player, Villager villager, ItemStack item) {
        if (!Config.Server.GIFTS_ENABLED.get()) return false;
        if (!item.is(Thief.Tags.Items.VILLAGER_GIFTS)) return false;
        int minorPositiveRep = villager.getGossips().getReputation(player.getUUID(), gossipType -> gossipType == GossipType.MINOR_POSITIVE);
        int minorNegativeRep = villager.getGossips().getReputation(player.getUUID(), gossipType -> gossipType == GossipType.MINOR_NEGATIVE);
        return minorPositiveRep < GossipType.MINOR_POSITIVE.max || minorNegativeRep < 0;
    }

    public static void onPlayerTick(Player player) {
        if (ThiefCommand.showNoticeDistanceAndWitnesses
                && player instanceof ServerPlayer serverPlayer
                && player.level().getGameTime() % 3 == 0) {

            List<LivingEntity> witnesses = Witness.getWitnesses(player);
            for (LivingEntity witness : witnesses) {
                witness.addEffect(new MobEffectInstance(MobEffects.GLOWING, 4));
            }

            double radius = Config.Server.WITNESS_MAX_DISTANCE.get() * Stealth.getVisibility(serverPlayer);
            int particles = 64; // number of particles in the ring

            for (int i = 0; i < particles; i++) {
                double angle = 2 * Math.PI * i / particles;
                double x = player.getX() + radius * Math.cos(angle);
                double z = player.getZ() + radius * Math.sin(angle);
                double y = player.getY() + 1;

                serverPlayer.serverLevel().sendParticles(serverPlayer, ParticleTypes.EXPLOSION, true, x, y, z, 1, 0, 0, 0, 0);
            }
        }
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher,
                                        CommandBuildContext context, Commands.CommandSelection environment) {
        ThiefCommand.register(dispatcher);
    }
}
