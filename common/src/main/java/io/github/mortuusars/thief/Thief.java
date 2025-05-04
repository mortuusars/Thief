package io.github.mortuusars.thief;

import com.google.common.base.Preconditions;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatFormatter;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Thief {
    public static final String ID = "thief";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        Blocks.init();
        BlockEntityTypes.init();
        EntityTypes.init();
        Items.init();
        DataComponents.init();
        CriteriaTriggers.init();
        ItemSubPredicates.init();
        MenuTypes.init();
        RecipeSerializers.init();
        SoundEvents.init();
        ArgumentTypes.init();
    }

    /**
     * Creates resource location in the mod namespace with the given path.
     */
    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    public static class Blocks {
        static void init() {
        }
    }

    public static class BlockEntityTypes {
        static void init() {
        }
    }

    public static class Items {
        static void init() {
        }
    }

    public static class DataComponents {
        static void init() {
        }
    }

    public static class EntityTypes {
        static void init() {
        }
    }

    public static class MenuTypes {
        static void init() {
        }
    }

    public static class RecipeSerializers {
        static void init() {
        }
    }

    public static class SoundEvents {
        private static Supplier<SoundEvent> register(String category, String key) {
            Preconditions.checkState(category != null && !category.isEmpty(), "'category' should not be empty.");
            Preconditions.checkState(key != null && !key.isEmpty(), "'key' should not be empty.");
            String path = category + "." + key;
            return Register.soundEvent(path, () -> SoundEvent.createVariableRangeEvent(Thief.resource(path)));
        }

        static void init() {
        }
    }

    public static class Stats {
        public static final Map<ResourceLocation, StatFormatter> STATS = new HashMap<>();

        @SuppressWarnings("SameParameterValue")
        private static ResourceLocation register(ResourceLocation location, StatFormatter formatter) {
            STATS.put(location, formatter);
            return location;
        }

        public static void register() {
            STATS.forEach((location, formatter) -> {
                net.minecraft.core.Registry.register(BuiltInRegistries.CUSTOM_STAT, location, location);
                net.minecraft.stats.Stats.CUSTOM.get(location, formatter);
            });
        }
    }

    public static class CriteriaTriggers {
        public static void init() {
        }
    }

    public static class ItemSubPredicates {
        public static void init() {
        }
    }

    public static class LootTables {
    }

    public static class Tags {
        public static class Items {
            public static final TagKey<Item> VILLAGER_GIFTS =
                    TagKey.create(net.minecraft.core.registries.Registries.ITEM, resource("villager_gifts"));
        }

        public static class Blocks {
            public static final TagKey<Block> PROTECTED_LIGHT =
                    TagKey.create(net.minecraft.core.registries.Registries.BLOCK, resource("protected/light"));
            public static final TagKey<Block> PROTECTED_MODERATE =
                    TagKey.create(net.minecraft.core.registries.Registries.BLOCK, resource("protected/moderate"));
            public static final TagKey<Block> PROTECTED_HEAVY =
                    TagKey.create(net.minecraft.core.registries.Registries.BLOCK, resource("protected/heavy"));
        }

        public static class Structures {
            public static final TagKey<Structure> PROTECTED =
                    TagKey.create(net.minecraft.core.registries.Registries.STRUCTURE, resource("protected"));
        }
    }

    public static class ArgumentTypes {
        public static void init() {
        }
    }

    public static class Registries {
    }

    public static class ReputationEvents {
        public static final ReputationEventType THEFT_LIGHT = ReputationEventType.register("theft_light");
        public static final ReputationEventType THEFT_MODERATE = ReputationEventType.register("theft_moderate");
        public static final ReputationEventType THEFT_HEAVY = ReputationEventType.register("theft_heavy");
    }
}
