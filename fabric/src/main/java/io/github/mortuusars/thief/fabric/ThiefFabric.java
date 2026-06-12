package io.github.mortuusars.thief.fabric;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import io.github.mortuusars.thief.Config;
import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.event.CommonEvents;
import io.github.mortuusars.thief.event.ServerEvents;
import io.github.mortuusars.thief.network.fabric.FabricC2SPackets;
import io.github.mortuusars.thief.network.fabric.FabricS2CPackets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.config.ModConfig;
import org.jetbrains.annotations.Nullable;

public class ThiefFabric implements ModInitializer {
    // Server field to access when no other objects are available to get it from.
    public static @Nullable MinecraftServer server = null;

    @Override
    public void onInitialize() {
        Thief.init();

        ConfigRegistry.INSTANCE.register(Thief.ID, ModConfig.Type.SERVER, Config.Server.SPEC);
        ConfigRegistry.INSTANCE.register(Thief.ID, ModConfig.Type.CLIENT, Config.Client.SPEC);

        CommandRegistrationCallback.EVENT.register(CommonEvents::registerCommands);

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            ThiefFabric.server = server;
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            ThiefFabric.server = null;
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            return CommonEvents.onEntityInteracted(player, hand, entity);
        });

        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (player instanceof ServerPlayer serverPlayer) {
                ServerEvents.onBlockDestroyedByPlayer(serverPlayer, pos, state);
            }
            return true;
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((LivingEntity entity, DamageSource damageSource) -> {
            if (damageSource.getEntity() instanceof ServerPlayer player) {
                ServerEvents.onEntityKilled(player, entity, damageSource);
            }
        });

        Thief.Stats.register();

        FabricC2SPackets.register();
        FabricS2CPackets.register();
    }
}
