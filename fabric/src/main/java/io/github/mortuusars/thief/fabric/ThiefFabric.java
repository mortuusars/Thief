package io.github.mortuusars.thief.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import io.github.mortuusars.thief.Config;
import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.event.CommonEvents;
import io.github.mortuusars.thief.network.fabric.FabricC2SPackets;
import io.github.mortuusars.thief.network.fabric.FabricS2CPackets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionResult;
import net.neoforged.fml.config.ModConfig;
import org.jetbrains.annotations.Nullable;

public class ThiefFabric implements ModInitializer {
    // Server field to access when no other objects are available to get it from.
    public static @Nullable MinecraftServer server = null;

    @Override
    public void onInitialize() {
        Thief.init();

        NeoForgeConfigRegistry.INSTANCE.register(Thief.ID, ModConfig.Type.SERVER, Config.Server.SPEC);
        NeoForgeConfigRegistry.INSTANCE.register(Thief.ID, ModConfig.Type.CLIENT, Config.Client.SPEC);

        CommandRegistrationCallback.EVENT.register(CommonEvents::registerCommands);

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            ThiefFabric.server = server;
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            ThiefFabric.server = null;
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (hitResult != null) return InteractionResult.PASS;
            return CommonEvents.onEntityInteracted(player, hand, entity);
        });

        FabricC2SPackets.register();
        FabricS2CPackets.register();
    }
}
