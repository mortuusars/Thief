package io.github.mortuusars.thief.neoforge;

import com.google.common.base.Preconditions;
import io.github.mortuusars.thief.Config;
import io.github.mortuusars.thief.Thief;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;

@Mod(Thief.ID)
public class ThiefNeoForge {
    public ThiefNeoForge(ModContainer container) {
        Thief.init();

        container.registerConfig(ModConfig.Type.SERVER, Config.Server.SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, Config.Client.SPEC);

        @Nullable IEventBus modEventBus = container.getEventBus();
        Preconditions.checkNotNull(modEventBus);

        RegisterImpl.BLOCKS.register(modEventBus);
        RegisterImpl.BLOCK_ENTITY_TYPES.register(modEventBus);
        RegisterImpl.ENTITY_TYPES.register(modEventBus);
        RegisterImpl.ITEMS.register(modEventBus);
        RegisterImpl.MENU_TYPES.register(modEventBus);
        RegisterImpl.RECIPE_TYPES.register(modEventBus);
        RegisterImpl.RECIPE_SERIALIZERS.register(modEventBus);
        RegisterImpl.CRITERION_TRIGGERS.register(modEventBus);
        RegisterImpl.ITEM_SUB_PREDICATES.register(modEventBus);
        RegisterImpl.SOUND_EVENTS.register(modEventBus);
        RegisterImpl.COMMAND_ARGUMENT_TYPES.register(modEventBus);
        RegisterImpl.WORLD_GEN_FEATURES.register(modEventBus);
        RegisterImpl.DATA_COMPONENT_TYPES.register(modEventBus);
        RegisterImpl.PARTICLE_TYPES.register(modEventBus);
        RegisterImpl.CUSTOM_STATS.register(modEventBus);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ThiefNeoForgeClient.init(container);
        }
    }
}