package io.github.mortuusars.thief.fabric;

import io.github.mortuusars.thief.fabric.api.event.CrimeCommitedCallback;
import io.github.mortuusars.thief.fabric.api.event.GiftGivenCallback;
import io.github.mortuusars.thief.fabric.api.event.ReputationLevelChangedCallback;
import io.github.mortuusars.thief.world.Crime;
import io.github.mortuusars.thief.world.Reputation;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PlatformHelperImpl {
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static void fireCrimeCommitedEvent(LivingEntity criminal, Crime crime, List<LivingEntity> witnesses) {
        CrimeCommitedCallback.EVENT.invoker().crimeCommited(criminal, crime, witnesses);
    }

    public static void fireGiftGivenEvent(ServerPlayer player, Villager villager, ItemStack gift) {
        GiftGivenCallback.EVENT.invoker().giftGiven(player, villager, gift);
    }

    public static void fireReputationLevelChangedEvent(LivingEntity criminal, Villager villager, Reputation oldReputation, Reputation newReputation) {
        ReputationLevelChangedCallback.EVENT.invoker().giftGiven(criminal, villager, oldReputation, newReputation);
    }
}
