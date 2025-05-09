package io.github.mortuusars.thief.client;

import io.github.mortuusars.thief.Config;
import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.network.Packets;
import io.github.mortuusars.thief.network.packet.serverbound.QueryVillagerReputationC2SP;
import io.github.mortuusars.thief.world.Reputation;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.EntityHitResult;

import java.util.ArrayList;

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
        if (!Config.Client.SHOW_VILLAGER_REPUTATION_TOOLTIP.get()) return;

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

        ArrayList<FormattedCharSequence> lines = new ArrayList<>();
        lines.add(Component.translatable("gui.thief.reputation", name).getVisualOrderText());
        lines.addAll(Minecraft.getInstance().font.split(reputation.getLocalizedDescription(), 170));

        if (Minecraft.getInstance().options.advancedItemTooltips && lastGossipsVillagerId == villager.getId()) {
            lines.add(Component.translatable("gui.thief.gossips").getVisualOrderText());
            lines.add(Component.translatable("gui.thief.gossips.major_negative", "§8" + lastMajorNegative).getVisualOrderText());
            lines.add(Component.translatable("gui.thief.gossips.minor_negative", "§8" + lastMinorNegative).getVisualOrderText());
            lines.add(Component.translatable("gui.thief.gossips.minor_positive", "§8" + lastMinorPositive).getVisualOrderText());
            lines.add(Component.translatable("gui.thief.gossips.major_positive", "§8" + lastMajorPositive).getVisualOrderText());
            lines.add(Component.translatable("gui.thief.gossips.trading", "§8" + lastTrading).getVisualOrderText());
        }

        int x = minecraft.getWindow().getGuiScaledWidth() / 2 + 8;
        int y = minecraft.getWindow().getGuiScaledHeight() / 2 - (int)(lines.size() / 2f * 9f);
        guiGraphics.renderTooltip(minecraft.font, lines, x, y + 10);
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
