package io.github.mortuusars.thief.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.mortuusars.thief.world.Crime;
import io.github.mortuusars.thief.world.Offence;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ThiefCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("thief")
                .requires((stack) -> stack.hasPermission(2))
                .then(Commands.literal("is_in_protected_structure")
                        .executes(ThiefCommand::isInProtectedStructure))
                .then(Commands.literal("crime")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.literal("light")
                                        .executes(context -> commitCrime(context, Offence.LIGHT)))
                                .then(Commands.literal("moderate")
                                        .executes(context -> commitCrime(context, Offence.MODERATE)))
                                .then(Commands.literal("heavy")
                                        .executes(context -> commitCrime(context, Offence.HEAVY))))));
    }

    private static int isInProtectedStructure(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        context.getSource().sendSuccess(() -> Component.literal("Is in '#thief:protected' structure: " +
                Crime.isInProtectedStructure(context.getSource().getLevel(), player.blockPosition())), true);
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
