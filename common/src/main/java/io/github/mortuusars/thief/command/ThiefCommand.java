package io.github.mortuusars.thief.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.mortuusars.thief.world.Crime;
import io.github.mortuusars.thief.world.Offence;
import io.github.mortuusars.thief.world.Reputation;
import io.github.mortuusars.thief.world.Witness;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;

import java.util.List;

public class ThiefCommand {
    public static boolean showNoticeDistanceAndWitnesses = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("thief")
                .requires((stack) -> stack.hasPermission(2))
                .then(Commands.literal("debug")
                        .then(Commands.literal("is_in_protected_structure")
                                .executes(ThiefCommand::isInProtectedStructure))
                        .then(Commands.literal("show_notice_distance_and_witnesses")
                                .executes(ThiefCommand::showNoticeDistance))
                        .then(Commands.literal("show_witness_reputation")
                                .executes(ThiefCommand::showWitnessReputation))
                        .then(Commands.literal("crime")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.literal("light")
                                                .executes(context -> commitCrime(context, Offence.LIGHT)))
                                        .then(Commands.literal("moderate")
                                                .executes(context -> commitCrime(context, Offence.MODERATE)))
                                        .then(Commands.literal("heavy")
                                                .executes(context -> commitCrime(context, Offence.HEAVY)))))));
    }

    // Debug --

    private static int isInProtectedStructure(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        context.getSource().sendSuccess(() -> Component.literal("Is in '#thief:protected' structure: " +
                Crime.isInProtectedStructure(context.getSource().getLevel(), player.blockPosition())), true);
        return 0;
    }

    private static int showNoticeDistance(CommandContext<CommandSourceStack> context) {
        if (showNoticeDistanceAndWitnesses) {
            showNoticeDistanceAndWitnesses = false;
            context.getSource().sendSuccess(() -> Component.literal("Turned off thief notice distance showing."), true);
        } else {
            showNoticeDistanceAndWitnesses = true;
            context.getSource().sendSuccess(() -> Component.literal("Showing thief notice distance."), true);
        }
        return 0;
    }

    private static int showWitnessReputation(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();

        if (player.isCreative() || player.isSpectator()) {
            context.getSource().sendSuccess(() -> Component.literal("You need to be in survival mode to be noticed by others."), true);
            return 0;
        }

        List<Villager> villagers = Witness.getWitnesses(player)
                .stream()
                .filter(entity -> entity instanceof Villager)
                .map(entity -> (Villager) entity)
                .toList();

        if (villagers.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("No witnesses."), true);
            return 0;
        }

        int averageValue = Reputation.averageValueFromCrowd(villagers, player);
        Reputation reputation = Reputation.fromValue(averageValue);

        context.getSource().sendSuccess(() -> Component.literal("Average reputation of " + villagers.size() + " witnesses is: ")
                .append(reputation.getLocalizedNameWithColor())
                .append(Component.literal(" (" + averageValue + ")").withColor(reputation.getColor())), true);

        return 0;
    }

    private static int commitCrime(CommandContext<CommandSourceStack> context, Offence offence) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();

        Crime.Outcome outcome = Crime.commit(player.serverLevel(), player, player.blockPosition(), offence);
        if (outcome.punished()) {
            context.getSource().sendSuccess(() -> Component.literal(
                            player.getScoreboardName() + " has commited " + offence.getName() + " crime with "
                                    + outcome.witnesses().size() + " witnesses.")
                    .withStyle(ChatFormatting.RED), true);
        } else {
            context.getSource().sendSuccess(() -> Component.literal(
                            player.getScoreboardName() + " has commited " + offence.getName() + " crime, but no one saw that.")
                    .withStyle(ChatFormatting.RED), true);
        }

        return 0;
    }

    /*private static List<ServerPlayer> getTargetPlayers(CommandContext<CommandSourceStack> context) {
        try {
            return new ArrayList<>(EntityArgument.getPlayers(context, "targets"));
        } catch (CommandSyntaxException e) {
            return Collections.emptyList();
        }
    }*/
}
