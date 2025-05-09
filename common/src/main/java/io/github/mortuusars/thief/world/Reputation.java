package io.github.mortuusars.thief.world;

import io.github.mortuusars.thief.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;

import java.util.List;

public enum Reputation {
    HATED("hated", 0xFFFF4949),
    UNWELCOME("unwelcome", 0xFFFF884E),
    DISTRUSTED("distrusted", 0xFFFEB259),
    NEUTRAL("neutral", 0xFFFFF8B9),
    ACCEPTED("accepted", 0xFFEFFF82),
    RESPECTED("respected", 0xFF8EF057),
    HONORED("honored", 0xFF54FFC9);

    private final String name;
    private final int color;

    Reputation(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public MutableComponent getLocalizedName() {
        return Component.translatable("gui.thief.reputation." + name);
    }

    public MutableComponent getLocalizedNameWithColor() {
        return getLocalizedName().withStyle(Style.EMPTY.withColor(color));
    }

    public MutableComponent getLocalizedDescription() {
        return Component.translatable("gui.thief.reputation." + name + ".description");
    }

    public boolean isLowerOrEqualTo(Reputation reputation) {
        return ordinal() <= reputation.ordinal();
    }

    public boolean isGreaterOrEqualTo(Reputation reputation) {
        return ordinal() >= reputation.ordinal();
    }

    public boolean ignores(Crime crime) {
        return switch (crime) {
            case LIGHT -> isGreaterOrEqualTo(ACCEPTED);
            case MEDIUM -> isGreaterOrEqualTo(RESPECTED);
            case HEAVY -> isGreaterOrEqualTo(HONORED);
        };
    }

    public boolean canTrade() {
        return isGreaterOrEqualTo(Config.Server.TRADE_REPUTATION_THRESHOLD.get());
    }

    // --

    public static Reputation fromValue(Villager villager, LivingEntity entity) {
        int reputation = villager.getGossips().getReputation(entity.getUUID(), gossipType -> true);
        return fromValue(reputation);
    }

    public static Reputation fromValue(int reputation) {
        if (reputation <= -100) return HATED;
        if (reputation <= -50) return UNWELCOME;
        if (reputation < 0) return DISTRUSTED;
        if (reputation < 10) return NEUTRAL;
        if (reputation < 20) return ACCEPTED;
        if (reputation < 30) return RESPECTED;
        return HONORED;
    }

    public static int averageValueFromVillagers(LivingEntity subject, List<Villager> villagers) {
        return (int) villagers.stream()
                .mapToInt(villager -> villager.getGossips().getReputation(subject.getUUID(), gossipType -> true))
                .average().orElse(0);
    }

    public static Reputation averageFromVillagers(LivingEntity subject, List<Villager> villagers) {
        return fromValue(averageValueFromVillagers(subject, villagers));
    }

    public static int averageValueFromCrowd(LivingEntity entity, List<LivingEntity> crowd) {
        return (int) crowd.stream()
                .mapToInt(e -> valueOf(entity, e))
                .average().orElse(0);
    }

    public static Reputation averageFromCrowd(LivingEntity subject, List<LivingEntity> crowd) {
        return fromValue(averageValueFromCrowd(subject, crowd));
    }

    public static int valueOf(LivingEntity subject, LivingEntity entityWithReputation) {
        if (entityWithReputation instanceof Villager villager) {
            return villager.getGossips().getReputation(subject.getUUID(), gossipType -> true);
        }
        return 0;
    }
}
