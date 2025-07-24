package io.github.mortuusars.thief.neoforge.compat.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface ThiefJSEvents {
    EventGroup GROUP = EventGroup.of("ThiefEvents");

    EventHandler CRIME_COMMITED = GROUP.server("crimeCommited", () -> CrimeCommitedEventJS.class);
    EventHandler GIFT_GIVEN = GROUP.server("giftGiven", () -> GiftGivenEventJS.class);
    EventHandler REPUTATION_LEVEL_CHANGED = GROUP.server("reputationLevelChanged", () -> ReputationLevelChangedEventJS.class);
}