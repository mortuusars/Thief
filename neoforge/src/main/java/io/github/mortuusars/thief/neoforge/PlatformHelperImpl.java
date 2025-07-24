package io.github.mortuusars.thief.neoforge;

import io.github.mortuusars.thief.neoforge.api.event.CrimeCommitedEvent;
import io.github.mortuusars.thief.neoforge.api.event.GiftGivenEvent;
import io.github.mortuusars.thief.neoforge.api.event.ReputationLevelChangedEvent;
import io.github.mortuusars.thief.world.Crime;
import io.github.mortuusars.thief.world.Reputation;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;
import java.util.function.Consumer;

public class PlatformHelperImpl {
    public static void openMenu(ServerPlayer serverPlayer, MenuProvider menuProvider, Consumer<RegistryFriendlyByteBuf> extraDataWriter) {
        serverPlayer.openMenu(menuProvider, extraDataWriter);
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static boolean isInDevEnv() {
        return !FMLEnvironment.production;
    }

    public static void fireCrimeCommitedEvent(LivingEntity criminal, Crime crime, List<LivingEntity> witnesses) {
        NeoForge.EVENT_BUS.post(new CrimeCommitedEvent(criminal, crime, witnesses));
    }

    public static void fireGiftGivenEvent(ServerPlayer player, Villager villager, ItemStack gift) {
        NeoForge.EVENT_BUS.post(new GiftGivenEvent(player, villager, gift));
    }

    public static void fireReputationLevelChangedEvent(LivingEntity criminal, Villager villager, Reputation oldReputation, Reputation newReputation) {
        NeoForge.EVENT_BUS.post(new ReputationLevelChangedEvent(criminal, villager, oldReputation, newReputation));
    }
}
