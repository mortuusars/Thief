package io.github.mortuusars.thief.compat.kubejs.fabric;

import io.github.mortuusars.thief.compat.kubejs.ThiefKubeJSPlugin;
import io.github.mortuusars.thief.fabric.api.event.CrimeCommitedCallback;
import io.github.mortuusars.thief.fabric.api.event.GiftGivenCallback;
import io.github.mortuusars.thief.fabric.api.event.ReputationLevelChangedCallback;

public class ThiefKubeJSPluginImpl {
    public static void subscribeToEvents() {
        CrimeCommitedCallback.EVENT.register(ThiefKubeJSPlugin::fireCrimeCommitedEvent);
        GiftGivenCallback.EVENT.register(ThiefKubeJSPlugin::fireGiftGivenEvent);
        ReputationLevelChangedCallback.EVENT.register(ThiefKubeJSPlugin::fireReputationLevelChangedEvent);
    }
}