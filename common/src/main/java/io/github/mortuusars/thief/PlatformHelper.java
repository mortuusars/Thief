package io.github.mortuusars.thief;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.mortuusars.thief.world.Crime;
import io.github.mortuusars.thief.world.Reputation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class PlatformHelper {
    @ExpectPlatform
    public static void openMenu(ServerPlayer serverPlayer, MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraDataWriter) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isInDevEnv() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void fireCrimeCommitedEvent(LivingEntity criminal, Crime crime, List<LivingEntity> witnesses) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void fireGiftGivenEvent(ServerPlayer player, Villager villager, ItemStack gift) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void fireReputationLevelChangedEvent(LivingEntity criminal, Villager villager, Reputation oldReputation, Reputation newReputation) {
        throw new AssertionError();
    }
}
