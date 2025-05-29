package io.github.mortuusars.thief.forge.event;

import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.event.CommonEvents;
import io.github.mortuusars.thief.event.ServerEvents;
import io.github.mortuusars.thief.network.forge.PacketsImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ForgeCommonEvents {
    @Mod.EventBusSubscriber(modid = Thief.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                PacketsImpl.register();
                Thief.CriteriaTriggers.register();
                Thief.Stats.register();
            });
        }
    }

    @Mod.EventBusSubscriber(modid = Thief.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class GameBus {
        @SubscribeEvent
        public static void onBlockDestroyed(BlockEvent.BreakEvent event) {
            if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
                ServerEvents.onBlockDestroyedByPlayer(serverPlayer, event.getPos(), event.getState());
            }
        }

        @SubscribeEvent
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                ServerEvents.onBlockInteract(serverPlayer, event.getPos(), event.getHand());
            }
        }

        @SubscribeEvent
        public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
            if (CommonEvents.onEntityInteracted(event.getEntity(), event.getHand(), event.getTarget()) != InteractionResult.PASS) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onLivingDeath(LivingDeathEvent event) {
            if (event.getSource().getEntity() instanceof ServerPlayer player) {
                ServerEvents.onEntityKilled(player, event.getEntity(), event.getSource());
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                CommonEvents.onPlayerTick(event.player);
            }
        }

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            CommonEvents.registerCommands(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
        }
    }
}