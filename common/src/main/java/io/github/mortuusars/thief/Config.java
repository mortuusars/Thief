package io.github.mortuusars.thief;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Using ForgeConfigApiPort on fabric allows using forge config in both environments and without extra dependencies on forge.
 */
public class Config {
    public static class Server {
        public static final ModConfigSpec SPEC;

        // Misc
        public static final ModConfigSpec.BooleanValue FIX_SHIFT_CLICK_TRADE_REPUTATION;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

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