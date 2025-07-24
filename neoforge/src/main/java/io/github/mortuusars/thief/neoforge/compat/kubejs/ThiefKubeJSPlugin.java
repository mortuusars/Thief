package io.github.mortuusars.thief.neoforge.compat.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import io.github.mortuusars.thief.neoforge.api.event.CrimeCommitedEvent;
import io.github.mortuusars.thief.neoforge.api.event.GiftGivenEvent;
import io.github.mortuusars.thief.neoforge.api.event.ReputationLevelChangedEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;

public class ThiefKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(ThiefJSEvents.GROUP);
    }

    @Override
    public void init() {
        NeoForge.EVENT_BUS.addListener(EventPriority.LOW, this::postCrimeCommitedEvent);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOW, this::postGiftGivenEvent);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOW, this::postReputationLevelChangedEvent);
    }

    private void postCrimeCommitedEvent(CrimeCommitedEvent event) {
        ThiefJSEvents.CRIME_COMMITED.post(ScriptType.SERVER, new CrimeCommitedEventJS(event.criminal, event.crime, event.witnesses));
    }

    private void postGiftGivenEvent(GiftGivenEvent event) {
        ThiefJSEvents.GIFT_GIVEN.post(ScriptType.SERVER, new GiftGivenEventJS(event.player, event.villager, event.gift));
    }

    private void postReputationLevelChangedEvent(ReputationLevelChangedEvent event) {
        ThiefJSEvents.REPUTATION_LEVEL_CHANGED.post(ScriptType.SERVER, new ReputationLevelChangedEventJS(event.criminal, event.villager, event.oldReputation, event.newReputation));
    }
}
