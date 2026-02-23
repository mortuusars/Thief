package io.github.mortuusars.thief;

import com.google.common.base.Preconditions;
import com.mojang.logging.LogUtils;
import io.github.mortuusars.thief.advancement.trigger.CrimeCommitedTrigger;
import io.github.mortuusars.thief.advancement.trigger.GuardAttacksCriminalTrigger;
import io.github.mortuusars.thief.advancement.trigger.VillagerGiftTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatFormatter;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
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
        CriteriaTriggers.init();
    }

    /**
     * Creates resource location in the mod namespace with the given path.
     */
    public static Identifier resource(String path) {
        return Identifier.fromNamespaceAndPath(ID, path);
    }

    public static class Stats {
        public static final Map<Identifier, StatFormatter> STATS = new HashMap<>();

        public static final Identifier CAUGHT_COMMITING_LIGHT_CRIMES =
              register(resource("caught_commiting_light_crimes"), StatFormatter.DEFAULT);
        public static final Identifier CAUGHT_COMMITING_MEDIUM_CRIMES =
              register(resource("caught_commiting_medium_crimes"), StatFormatter.DEFAULT);
        public static final Identifier CAUGHT_COMMITING_HEAVY_CRIMES =
              register(resource("caught_commiting_heavy_crimes"), StatFormatter.DEFAULT);

        @SuppressWarnings("SameParameterValue")
        private static Identifier register(Identifier location, StatFormatter formatter) {
            STATS.put(location, formatter);
            return location;
        }

        public static void register() {
            STATS.forEach((location, formatter) -> {
                Registry.register(BuiltInRegistries.CUSTOM_STAT, location, location);
                net.minecraft.stats.Stats.CUSTOM.get(location, formatter);
            });
        }
    }

    public static class CriteriaTriggers {
        public static Supplier<CrimeCommitedTrigger> CRIME_COMMITED = Register.criterionTrigger("crime_committed", CrimeCommitedTrigger::new);
        public static Supplier<VillagerGiftTrigger> VILLAGER_GIFT = Register.criterionTrigger("villager_gift", VillagerGiftTrigger::new);
        public static Supplier<GuardAttacksCriminalTrigger> GUARD_ATTACKS_CRIMINAL = Register.criterionTrigger("guard_attacks_criminal", GuardAttacksCriminalTrigger::new);

        public static void init() {
        }
    }

    public static class Tags {
        public static class Items {
            public static final TagKey<Item> VILLAGER_GIFTS =
                    TagKey.create(net.minecraft.core.registries.Registries.ITEM, resource("villager_gifts"));
        }

        public static class Blocks {
            public static final TagKey<Block> BREAK_PROTECTED_LIGHT =
                    TagKey.create(net.minecraft.core.registries.Registries.BLOCK, resource("break_protected/light"));
            public static final TagKey<Block> BREAK_PROTECTED_MEDIUM =
                    TagKey.create(net.minecraft.core.registries.Registries.BLOCK, resource("break_protected/medium"));
            public static final TagKey<Block> BREAK_PROTECTED_HEAVY =
                    TagKey.create(net.minecraft.core.registries.Registries.BLOCK, resource("break_protected/heavy"));

            public static final TagKey<Block> INTERACT_PROTECTED_LIGHT =
                    TagKey.create(net.minecraft.core.registries.Registries.BLOCK, resource("interact_protected/light"));
            public static final TagKey<Block> INTERACT_PROTECTED_MEDIUM =
                    TagKey.create(net.minecraft.core.registries.Registries.BLOCK, resource("interact_protected/medium"));
            public static final TagKey<Block> INTERACT_PROTECTED_HEAVY =
                    TagKey.create(net.minecraft.core.registries.Registries.BLOCK, resource("interact_protected/heavy"));
        }

        public static class EntityTypes {
            public static final TagKey<EntityType<?>> KILLING_PROTECTED_LIGHT =
                    TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, resource("killing_protected/light"));
            public static final TagKey<EntityType<?>> KILLING_PROTECTED_MEDIUM =
                    TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, resource("killing_protected/medium"));
            public static final TagKey<EntityType<?>> KILLING_PROTECTED_HEAVY =
                    TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, resource("killing_protected/heavy"));

            public static final TagKey<EntityType<?>> WITNESSES =
                    TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, resource("witnesses"));

            public static final TagKey<EntityType<?>> GUARDS =
                    TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, resource("guards"));
        }

        public static class Structures {
            public static final TagKey<Structure> PROTECTED =
                    TagKey.create(net.minecraft.core.registries.Registries.STRUCTURE, resource("protected"));
        }
    }
}
