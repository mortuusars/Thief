package io.github.mortuusars.thief.compat;

import io.github.mortuusars.thief.PlatformHelper;

public class Mods {
    public static final Mod LITHOSTITCHED = new Mod("lithostitched");

    public record Mod(String id) {
        public boolean isLoaded() {
            return PlatformHelper.isModLoaded(id);
        }
    }
}