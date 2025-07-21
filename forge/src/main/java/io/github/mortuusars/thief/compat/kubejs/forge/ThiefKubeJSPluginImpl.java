package io.github.mortuusars.thief.compat.kubejs.forge;

import dev.latvian.mods.kubejs.script.ScriptType;
import io.github.mortuusars.thief.compat.kubejs.CrimeCommitedEventJS;
import io.github.mortuusars.thief.compat.kubejs.GiftGivenEventJS;
import io.github.mortuusars.thief.compat.kubejs.ReputationLevelChangedEventJS;
import io.github.mortuusars.thief.compat.kubejs.ThiefJSEvents;
import io.github.mortuusars.thief.forge.api.event.CrimeCommitedEvent;
import io.github.mortuusars.thief.forge.api.event.GiftGivenEvent;
import io.github.mortuusars.thief.forge.api.event.ReputationLevelChangedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;

public class ThiefKubeJSPluginImpl {
    public static void subscribeToEvents() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, ThiefKubeJSPluginImpl::fireCrimeCommitedEvent);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, ThiefKubeJSPluginImpl::fireGiftGivenEvent);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, ThiefKubeJSPluginImpl::fireReputationLevelChangedEvent);
    }

    private static void fireCrimeCommitedEvent(CrimeCommitedEvent event) {
        ThiefJSEvents.CRIME_COMMITED.post(ScriptType.SERVER, new CrimeCommitedEventJS(event.criminal, event.crime, event.witnesses));
    }

    private static void fireGiftGivenEvent(GiftGivenEvent event) {
        ThiefJSEvents.GIFT_GIVEN.post(ScriptType.SERVER, new GiftGivenEventJS(event.player, event.villager, event.gift));
    }

    private static void fireReputationLevelChangedEvent(ReputationLevelChangedEvent event) {
        ThiefJSEvents.REPUTATION_LEVEL_CHANGED.post(ScriptType.SERVER, new ReputationLevelChangedEventJS(event.criminal, event.villager, event.oldReputation, event.newReputation));
    }
}