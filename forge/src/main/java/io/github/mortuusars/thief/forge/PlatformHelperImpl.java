package io.github.mortuusars.thief.forge;

import io.github.mortuusars.thief.forge.api.event.CrimeCommitedEvent;
import io.github.mortuusars.thief.forge.api.event.GiftGivenEvent;
import io.github.mortuusars.thief.forge.api.event.ReputationLevelChangedEvent;
import io.github.mortuusars.thief.world.Crime;
import io.github.mortuusars.thief.world.Reputation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;
import java.util.function.Consumer;

public class PlatformHelperImpl {
    public static boolean canShear(ItemStack stack) {
        return stack.canPerformAction(ToolActions.SHEARS_CARVE);
    }

    public static boolean canStrip(ItemStack stack) {
        return stack.canPerformAction(ToolActions.AXE_STRIP);
    }

    public static void openMenu(ServerPlayer serverPlayer, MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraDataWriter) {
        NetworkHooks.openScreen(serverPlayer, menuProvider, extraDataWriter);
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static boolean isModLoading(String modId) {
        return LoadingModList.get().getModFileById(modId) != null;
    }

    public static void fireCrimeCommitedEvent(LivingEntity criminal, Crime crime, List<LivingEntity> witnesses) {
        MinecraftForge.EVENT_BUS.post(new CrimeCommitedEvent(criminal, crime, witnesses));
    }

    public static void fireGiftGivenEvent(ServerPlayer player, Villager villager, ItemStack gift) {
        MinecraftForge.EVENT_BUS.post(new GiftGivenEvent(player, villager, gift));
    }

    public static void fireReputationLevelChangedEvent(LivingEntity criminal, Villager villager, Reputation oldReputation, Reputation newReputation) {
        MinecraftForge.EVENT_BUS.post(new ReputationLevelChangedEvent(criminal, villager, oldReputation, newReputation));
    }
}
