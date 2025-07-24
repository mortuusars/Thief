package io.github.mortuusars.thief.fabric;

import io.github.mortuusars.thief.fabric.api.event.CrimeCommitedCallback;
import io.github.mortuusars.thief.fabric.api.event.GiftGivenCallback;
import io.github.mortuusars.thief.fabric.api.event.ReputationLevelChangedCallback;
import io.github.mortuusars.thief.world.Crime;
import io.github.mortuusars.thief.world.Reputation;
import io.netty.buffer.ByteBufUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class PlatformHelperImpl {
    public static void openMenu(ServerPlayer serverPlayer, MenuProvider menuProvider, Consumer<RegistryFriendlyByteBuf> extraDataWriter) {
        ExtendedScreenHandlerFactory<byte[]> extendedScreenHandlerFactory = new ExtendedScreenHandlerFactory<byte[]>() {
            @Override
            public byte[] getScreenOpeningData(ServerPlayer player) {
                RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(PacketByteBufs.create(), player.registryAccess());
                extraDataWriter.accept(buffer);
                byte[] bytes = ByteBufUtil.getBytes(buffer);
                buffer.release();
                return bytes;
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
                return menuProvider.createMenu(i, inventory, player);
            }

            @Override
            public @NotNull Component getDisplayName() {
                return menuProvider.getDisplayName();
            }
        };

        serverPlayer.openMenu(extendedScreenHandlerFactory);
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static boolean isInDevEnv() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
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
