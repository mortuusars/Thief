package io.github.mortuusars.thief.neoforge;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ThiefNeoForgeClient {
    public static void init(ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
