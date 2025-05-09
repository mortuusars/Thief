package io.github.mortuusars.thief;

import io.github.mortuusars.thief.world.PotentialCrime;
import io.github.mortuusars.thief.world.Reputation;
import net.minecraft.world.entity.ai.gossip.GossipType;
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
        public static final ModConfigSpec.BooleanValue CRIME_ONLY_IN_PROTECTED_STRUCTURE;

        // Witness
        public static final ModConfigSpec.IntValue WITNESS_MAX_DISTANCE;
        public static final ModConfigSpec.IntValue WITNESS_ALWAYS_NOTICE_DISTANCE;

        // Punishment
        public static final ModConfigSpec.EnumValue<Reputation> TRADE_REPUTATION_THRESHOLD;
        public static final ModConfigSpec.EnumValue<PotentialCrime> GUARD_ATTACK_THRESHOLD;
        // Reputation
        public static final ModConfigSpec.IntValue PUNISHMENT_LIGHT_MAJOR_NEGATIVE;
        public static final ModConfigSpec.IntValue PUNISHMENT_LIGHT_MINOR_NEGATIVE;
        public static final ModConfigSpec.IntValue PUNISHMENT_MEDIUM_MAJOR_NEGATIVE;
        public static final ModConfigSpec.IntValue PUNISHMENT_MEDIUM_MINOR_NEGATIVE;
        public static final ModConfigSpec.IntValue PUNISHMENT_HEAVY_MAJOR_NEGATIVE;
        public static final ModConfigSpec.IntValue PUNISHMENT_HEAVY_MINOR_NEGATIVE;

        // Gifts
        public static final ModConfigSpec.BooleanValue GIFTS_ENABLED;
        public static final ModConfigSpec.IntValue GIFTS_MINOR_POSITIVE_INCREASE;
        public static final ModConfigSpec.IntValue GIFTS_MINOR_NEGATIVE_REDUCTION;
        public static final ModConfigSpec.BooleanValue REQUIRES_SNEAK;

        // Hero of the Village
        public static final ModConfigSpec.BooleanValue HERO_OF_THE_VILLAGE_CAN_STEAL;
        public static final ModConfigSpec.IntValue HERO_MAJOR_POSITIVE_INCREASE;
        public static final ModConfigSpec.IntValue HERO_MINOR_POSITIVE_INCREASE;

        // Misc
        public static final ModConfigSpec.BooleanValue FIX_SHIFT_CLICK_TRADE_REPUTATION;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

            {
                builder.push("crime");
                CRIME_FOR_BREAKING_PROTECTED_BLOCKS = builder
                        .comment(" Breaking protected blocks (#thief:break_protected/<level>) in protected structure (#thief:protected) is considered a crime.",
                                " Default: true")
                        .define("breaking_protected_blocks", true);
                CRIME_FOR_INTERACTING_WITH_PROTECTED_BLOCKS = builder
                        .comment(" Interacting with protected blocks (#thief:interact_protected/<level>) in protected structure (#thief:protected) is considered a crime.",
                                " Default: true")
                        .define("interacting_with_protected_blocks", true);
                CRIME_FOR_KICKING_VILLAGER_OUT_OF_BED = builder
                        .comment(" Crime severity for kicking a villager out of bed.",
                                " Default: LIGHT")
                        .defineEnum("kicking_villager_out_of_bed", PotentialCrime.LIGHT);
                CRIME_FOR_SLEEPING_IN_VILLAGERS_BED = builder
                        .comment(" Crime severity for sleeping in a bed that belongs to a villager.",
                                " Default: MEDIUM")
                        .defineEnum("sleeping_in_villagers_bed", PotentialCrime.MEDIUM);
                CRIME_ONLY_IN_PROTECTED_STRUCTURE = builder
                        .comment(" Check for crimes only in #thief:protected structures. If disabled, whole world is 'protected'.",
                                " Default: true.")
                        .define("crime_only_in_protected_structures", true);
                builder.pop();
            }

            {
                builder.push("witness");
                WITNESS_MAX_DISTANCE = builder
                        .comment(" Max distance (in blocks) at which an entity can notice a crime.")
                        .defineInRange("max_notice_distance", 32, 1, 64);
                WITNESS_ALWAYS_NOTICE_DISTANCE = builder
                        .comment(" Distance (in blocks) at which an entity always notices a crime, even through blocks. This value is halved when entity is sleeping.")
                        .defineInRange("always_notice_distance", 6, 0, 64);
                builder.pop();
            }

            {
                builder.push("punishment");
                TRADE_REPUTATION_THRESHOLD = builder
                        .comment(" Minimum reputation level needed for a villager to trade with the player. Anything lower will prevent the trade.",
                                " Default: DISTRUSTED")
                        .defineEnum("trade_reputation_threshold", Reputation.DISTRUSTED);
                GUARD_ATTACK_THRESHOLD = builder
                        .comment(" Minimum crime severity at which guards (#thief:guards) will attack the player, if not already attacking something else. Set to NONE to disable.",
                                " Default: MEDIUM")
                        .defineEnum("guard_attack_threshold", PotentialCrime.MEDIUM);

                {
                    builder.push("reputation");
                    PUNISHMENT_LIGHT_MAJOR_NEGATIVE = builder
                            .comment(" [LIGHT] Value added to 'Major Negative' reputation gossip.")
                            .defineInRange("light_major_change", 0, 0, GossipType.MAJOR_NEGATIVE.max);
                    PUNISHMENT_LIGHT_MINOR_NEGATIVE = builder
                            .comment(" [LIGHT] Value added to 'Minor Negative' reputation gossip.")
                            .defineInRange("light_minor_change", 15, 0, GossipType.MINOR_NEGATIVE.max);
                    PUNISHMENT_MEDIUM_MAJOR_NEGATIVE = builder
                            .comment(" [MEDIUM] Value added to 'Major Negative' reputation gossip.")
                            .defineInRange("medium_major_change", 5, 0, GossipType.MAJOR_NEGATIVE.max);
                    PUNISHMENT_MEDIUM_MINOR_NEGATIVE = builder
                            .comment(" [MEDIUM] Value added to 'Minor Negative' reputation gossip.")
                            .defineInRange("medium_minor_change", 25, 0, GossipType.MINOR_NEGATIVE.max);
                    PUNISHMENT_HEAVY_MAJOR_NEGATIVE = builder
                            .comment(" [HEAVY] Value added to 'Major Negative' reputation gossip.")
                            .defineInRange("heavy_major_change", 20, 0, GossipType.MAJOR_NEGATIVE.max);
                    PUNISHMENT_HEAVY_MINOR_NEGATIVE = builder
                            .comment(" [HEAVY] Value added to 'Minor Negative' reputation gossip.")
                            .defineInRange("heavy_minor_change", 50, 0, GossipType.MINOR_NEGATIVE.max);
                    builder.pop();
                }

                builder.pop();
            }

            {
                builder.push("gifts");
                GIFTS_ENABLED = builder
                        .comment(" Villagers accept gifts (#thief:villager_gifts) that will improve player's reputation with them. Just use the item on a Villager.",
                                " Default: true")
                        .define("enabled", true);
                GIFTS_MINOR_POSITIVE_INCREASE = builder
                        .comment(" 'Minor Positive' reputation increase per gift.",
                                " Default: 2")
                        .defineInRange("minor_positive_increase", 2, 0, GossipType.MINOR_POSITIVE.max);
                GIFTS_MINOR_NEGATIVE_REDUCTION = builder
                        .comment(" 'Minor Negative' reputation reduction per gift.",
                                " Default: 5")
                        .defineInRange("minor_negative_reduction", 5, 0, GossipType.MINOR_NEGATIVE.max);
                REQUIRES_SNEAK = builder
                        .comment(" Giving gifts requires 'sneak' to be held.",
                                " Default: false")
                        .define("requires_sneak", false);
                builder.pop();
            }

            {
                builder.push("hero_of_the_village");
                HERO_OF_THE_VILLAGE_CAN_STEAL = builder
                        .comment(" Players with 'Hero of the Village' buff can steal without consequences.",
                                " Default: true")
                        .define("hero_can_steal", true);
                HERO_MAJOR_POSITIVE_INCREASE = builder
                        .comment(" 'Major Positive' reputation increase with nearby villagers when raid is defeated.",
                                " Default: 2")
                        .defineInRange("major_positive_increase", 2, 0, GossipType.MAJOR_POSITIVE.max);
                HERO_MINOR_POSITIVE_INCREASE = builder
                        .comment(" 'Minor Positive' reputation increase with nearby villagers when raid is defeated.",
                                " Default: 25")
                        .defineInRange("minor_positive_increase", 25, 0, GossipType.MINOR_POSITIVE.max);
                builder.pop();
            }

            {
                builder.push("misc");
                FIX_SHIFT_CLICK_TRADE_REPUTATION = builder
                        .comment(" Fix villager trade reputation (gossip) not applying properly when using shift+click to trade (shift+clicking is considered one trade operation, instead of many).",
                                " Default: true.")
                        .define("fix_shift_click_villager_trading_reputation", true);
                builder.pop();
            }

            SPEC = builder.build();
        }
    }

    public static class Client {
        public static final ModConfigSpec SPEC;

        public static final ModConfigSpec.BooleanValue SHOW_VILLAGER_REPUTATION_TOOLTIP;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

            SHOW_VILLAGER_REPUTATION_TOOLTIP = builder
                    .comment(" Tooltip with reputation info will be shown when looking at a Villager while holding an item tagged as '#thief:villager_gifts'.",
                            " Default: true.")
                    .define("show_villager_reputation_tooltip", true);

            SPEC = builder.build();
        }
    }
}