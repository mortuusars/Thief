package io.github.mortuusars.thief;

import io.github.mortuusars.thief.world.PotentialCrime;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Using ForgeConfigApiPort on fabric allows using forge config in both environments and without extra dependencies on forge.
 */
public class Config {
    public static class Server {
        public static final ModConfigSpec SPEC;

        // Crimes
        public static final ModConfigSpec.BooleanValue CRIME_FOR_BREAKING_PROTECTED_BLOCKS;
        public static final ModConfigSpec.BooleanValue CRIME_FOR_INTERACTING_WITH_PROTECTED_BLOCKS;
        public static final ModConfigSpec.EnumValue<PotentialCrime> CRIME_FOR_KICKING_VILLAGER_OUT_OF_BED;
        public static final ModConfigSpec.EnumValue<PotentialCrime> CRIME_FOR_SLEEPING_IN_VILLAGERS_BED;

        // Witness
        public static final ModConfigSpec.IntValue WITNESS_MAX_DISTANCE;
        public static final ModConfigSpec.IntValue WITNESS_ALWAYS_NOTICE_DISTANCE;

        // Offence
        public static final ModConfigSpec.IntValue OFFENCE_LIGHT_MAJOR_NEGATIVE;
        public static final ModConfigSpec.IntValue OFFENCE_LIGHT_MINOR_NEGATIVE;
        public static final ModConfigSpec.IntValue OFFENCE_MEDIUM_MAJOR_NEGATIVE;
        public static final ModConfigSpec.IntValue OFFENCE_MEDIUM_MINOR_NEGATIVE;
        public static final ModConfigSpec.IntValue OFFENCE_HEAVY_MAJOR_NEGATIVE;
        public static final ModConfigSpec.IntValue OFFENCE_HEAVY_MINOR_NEGATIVE;

        // Misc
        public static final ModConfigSpec.BooleanValue FIX_SHIFT_CLICK_TRADE_REPUTATION;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

            {
                builder.push("Crimes");
                CRIME_FOR_BREAKING_PROTECTED_BLOCKS = builder
                        .comment("Breaking protected blocks (#thief:break_protected/<level>) in protected structure (#thief:protected) is considered a crime.", "Default: true")
                        .define("breaking_protected_blocks", true);
                CRIME_FOR_INTERACTING_WITH_PROTECTED_BLOCKS = builder
                        .comment("Interacting with protected blocks (#thief:interact_protected/<level>) in protected structure (#thief:protected) is considered a crime.", "Default: true")
                        .define("interacting_with_protected_blocks", true);
                CRIME_FOR_KICKING_VILLAGER_OUT_OF_BED = builder
                        .comment("Crime severity for kicking a villager out of bed. Default: LIGHT")
                        .defineEnum("kicking_villager_out_of_bed", PotentialCrime.LIGHT);
                CRIME_FOR_SLEEPING_IN_VILLAGERS_BED = builder
                        .comment("Crime severity for sleeping in a bed that belongs to a villager. Default: MEDIUM")
                        .defineEnum("sleeping_in_villagers_bed", PotentialCrime.MEDIUM);
                builder.pop();
            }

            {
                builder.push("Witness");
                WITNESS_MAX_DISTANCE = builder
                        .comment("Max distance (in blocks) at which an entity can notice a crime.")
                        .defineInRange("max_notice_distance", 32, 1, 64);
                WITNESS_ALWAYS_NOTICE_DISTANCE = builder
                        .comment("Distance (in blocks) at which an entity always notices a crime, even through blocks. This value is halved when entity is sleeping.")
                        .defineInRange("always_notice_distance", 6, 0, 64);
                builder.pop();
            }

            {
                builder.push("Offence");
                OFFENCE_LIGHT_MAJOR_NEGATIVE = builder
                    .comment("[LIGHT] Value added to 'Major Negative' reputation gossip.")
                    .defineInRange("light_major_change", 0, 0, 100);
                OFFENCE_LIGHT_MINOR_NEGATIVE = builder
                        .comment("[LIGHT] Value added to 'Minor Negative' reputation gossip.")
                        .defineInRange("light_minor_change", 15, 0, 200);
                OFFENCE_MEDIUM_MAJOR_NEGATIVE = builder
                        .comment("[MEDIUM] Value added to 'Major Negative' reputation gossip.")
                        .defineInRange("medium_major_change", 5, 0, 100);
                OFFENCE_MEDIUM_MINOR_NEGATIVE = builder
                        .comment("[MEDIUM] Value added to 'Minor Negative' reputation gossip.")
                        .defineInRange("medium_minor_change", 25, 0, 200);
                OFFENCE_HEAVY_MAJOR_NEGATIVE = builder
                        .comment("[HEAVY] Value added to 'Major Negative' reputation gossip.")
                        .defineInRange("heavy_major_change", 20, 0, 100);
                OFFENCE_HEAVY_MINOR_NEGATIVE = builder
                        .comment("[HEAVY] Value added to 'Minor Negative' reputation gossip.")
                        .defineInRange("heavy_minor_change", 50, 0, 200);
                builder.pop();
            }

            {
                builder.push("Misc");
                FIX_SHIFT_CLICK_TRADE_REPUTATION = builder
                        .comment("Fix villager trade reputation (gossip) not applying properly when using shift+click to trade (shift+clicking is considered one trade operation, instead of many). Default: true.")
                        .define("fix_shift_click_villager_trading_reputation", true);
                builder.pop();
            }

            SPEC = builder.build();
        }
    }

    public static class Client {
        public static final ModConfigSpec SPEC;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

            SPEC = builder.build();
        }
    }
}