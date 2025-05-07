package io.github.mortuusars.thief.world;

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
            case MODERATE -> isGreaterOrEqualTo(RESPECTED);
            case HEAVY -> isGreaterOrEqualTo(HONORED);
        };
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

    public static int averageValueFromCrowd(List<Villager> crowd, LivingEntity entity) {
        return (int)crowd.stream()
                .mapToInt(villager -> villager.getGossips().getReputation(entity.getUUID(), gossipType -> true))
                .average().orElse(0);
    }

    public static Reputation averageFromCrowd(List<Villager> crowd, LivingEntity entity) {
        return fromValue(averageValueFromCrowd(crowd, entity));
    }
}
