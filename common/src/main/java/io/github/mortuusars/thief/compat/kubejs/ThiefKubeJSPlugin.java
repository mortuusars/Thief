package io.github.mortuusars.thief.compat.kubejs;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import io.github.mortuusars.thief.world.Crime;
import io.github.mortuusars.thief.world.Reputation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ThiefKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void init() {
        subscribeToEvents();
    }

    @ExpectPlatform
    public static void subscribeToEvents() {
        throw new AssertionError();
    }

    @Override
    public void registerEvents() {
        ThiefJSEvents.register();
    }

    public static void fireCrimeCommitedEvent(LivingEntity criminal, Crime crime, List<LivingEntity> witnesses) {
        ThiefJSEvents.CRIME_COMMITED.post(ScriptType.SERVER, new CrimeCommitedEventJS(criminal, crime, witnesses));
    }

    public static void fireGiftGivenEvent(ServerPlayer player, Villager villager, ItemStack gift) {
        ThiefJSEvents.GIFT_GIVEN.post(ScriptType.SERVER, new GiftGivenEventJS(player, villager, gift));
    }

    public static void fireReputationLevelChangedEvent(LivingEntity criminal, Villager villager, Reputation oldReputation, Reputation newReputation) {
        ThiefJSEvents.REPUTATION_LEVEL_CHANGED.post(ScriptType.SERVER, new ReputationLevelChangedEventJS(criminal, villager, oldReputation, newReputation));
    }
}
