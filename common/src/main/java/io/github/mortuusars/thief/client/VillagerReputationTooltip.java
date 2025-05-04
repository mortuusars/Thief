package io.github.mortuusars.thief.client;

import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.network.Packets;
import io.github.mortuusars.thief.network.packet.serverbound.QueryVillagerReputationC2SP;
import io.github.mortuusars.thief.world.Reputation;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.EntityHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VillagerReputationTooltip {
    private static int lastVillagerId = -1;
    private static long lastRequestTime = -1L;
    private static int lastReputation = 0;

    // Gossips
    private static int lastGossipsVillagerId = -1;
    private static int lastMajorNegative;
    private static int lastMinorNegative;
    private static int lastMinorPositive;
    private static int lastMajorPositive;
    private static int lastTrading;

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null || minecraft.player == null || minecraft.player.isSpectator() || minecraft.screen != null
                || !minecraft.player.getMainHandItem().is(Thief.Tags.Items.VILLAGER_GIFTS)
                || !(minecraft.hitResult instanceof EntityHitResult entityHitResult)
                || !(entityHitResult.getEntity() instanceof Villager villager)) {
            return;
        }

        long gameTime = minecraft.level.getGameTime();
        if (gameTime - lastRequestTime > 5) {
            Packets.sendToServer(new QueryVillagerReputationC2SP(villager.getId(), Minecraft.getInstance().options.advancedItemTooltips));
            lastRequestTime = gameTime;
        }

        if (lastVillagerId != villager.getId()) {
            return;
        }

        Reputation reputation = Reputation.fromValue(lastReputation);

        MutableComponent name = reputation.getLocalizedNameWithColor();
        if (Minecraft.getInstance().options.advancedItemTooltips) {
            name.append(" (" + lastReputation + ")");
        }
        List<Component> list = List.of(
                Component.translatable("gui.thief.reputation", name),
                reputation.getLocalizedDescription().withStyle(ChatFormatting.GRAY)
        );

        if (Minecraft.getInstance().options.advancedItemTooltips && lastGossipsVillagerId == villager.getId()) {
            ArrayList<Component> list1 = new ArrayList<>(list);
            list1.add(Component.translatable("gui.thief.gossips"));
            list1.add(Component.translatable("gui.thief.gossips.major_negative", "§8" + lastMajorNegative));
            list1.add(Component.translatable("gui.thief.gossips.minor_negative", "§8" + lastMinorNegative));
            list1.add(Component.translatable("gui.thief.gossips.minor_positive", "§8" + lastMinorPositive));
            list1.add(Component.translatable("gui.thief.gossips.major_positive", "§8" + lastMajorPositive));
            list1.add(Component.translatable("gui.thief.gossips.trading", "§8" + lastTrading));
            list = list1;
        }

        int x = minecraft.getWindow().getGuiScaledWidth() / 2 + 8;
        int y = minecraft.getWindow().getGuiScaledHeight() / 2 - (int)(list.size() / 2f * 9f);
        guiGraphics.renderTooltip(minecraft.font, list, Optional.empty(), x, y + 10);
    }

    public static void updateReputation(int villagerId, int reputation) {
        lastVillagerId = villagerId;
        lastReputation = reputation;
    }

    public static void updateGossips(int villagerId, int majorNegative, int minorNegative,
                                     int minorPositive, int majorPositive, int trading) {
        lastGossipsVillagerId = villagerId;
        lastMajorNegative = majorNegative;
        lastMinorNegative = minorNegative;
        lastMinorPositive = minorPositive;
        lastMajorPositive = majorPositive;
        lastTrading = trading;
    }
}
