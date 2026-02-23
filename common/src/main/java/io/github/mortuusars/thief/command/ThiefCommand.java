package io.github.mortuusars.thief.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.mortuusars.thief.Config;
import io.github.mortuusars.thief.world.Crime;
import io.github.mortuusars.thief.world.Reputation;
import io.github.mortuusars.thief.world.Witness;
import io.github.mortuusars.thief.world.stealth.Stealth;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.Villager;

import java.util.List;

public class ThiefCommand {
    private static boolean showNoticeDistanceAndWitnesses = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("thief")
                .requires((stack) -> stack.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                .then(Commands.literal("debug")
                        .then(Commands.literal("is_in_protected_structure")
                                .executes(ThiefCommand::isInProtectedStructure))
                        .then(Commands.literal("show_notice_distance_and_witnesses")
                                .executes(ThiefCommand::showNoticeDistance))
                        .then(Commands.literal("show_witness_reputation")
                                .executes(ThiefCommand::showWitnessReputation)))
                .then(Commands.literal("commit_crime")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.literal("light")
                                        .executes(context -> commitCrime(context, Crime.LIGHT)))
                                .then(Commands.literal("medium")
                                        .executes(context -> commitCrime(context, Crime.MEDIUM)))
                                .then(Commands.literal("heavy")
                                        .executes(context -> commitCrime(context, Crime.HEAVY))))));
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

        int averageValue = Reputation.averageValueFromVillagers(player, villagers);
        Reputation reputation = Reputation.fromValue(averageValue);

        context.getSource().sendSuccess(() -> Component.literal("Average reputation of " + villagers.size() + " witnesses is: ")
                .append(reputation.getLocalizedNameWithColor())
                .append(Component.literal(" (" + averageValue + ")").withColor(reputation.getColor())), true);

        return 0;
    }

    private static int commitCrime(CommandContext<CommandSourceStack> context, Crime crime) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();

        Crime.Outcome outcome = crime.commit(player.level(), player, player.blockPosition());
        if (outcome.punished()) {
            context.getSource().sendSuccess(() -> Component.literal(
                            player.getScoreboardName() + " has commited " + crime.getName() + " crime with "
                                    + outcome.witnesses().size() + " witnesses.")
                    .withStyle(ChatFormatting.RED), true);
        } else {
            context.getSource().sendSuccess(() -> Component.literal(
                            player.getScoreboardName() + " has commited " + crime.getName() + " crime, but no one saw that.")
                    .withStyle(ChatFormatting.RED), true);
        }

        return 0;
    }

    public static void onPlayerTick(ServerPlayer player) {
        if (showNoticeDistanceAndWitnesses && player.level().getGameTime() % 3 == 0) {
            List<LivingEntity> witnesses = Witness.getWitnesses(player);
            for (LivingEntity witness : witnesses) {
                witness.addEffect(new MobEffectInstance(MobEffects.GLOWING, 4));
            }

            double radius = Config.Server.WITNESS_MAX_DISTANCE.get() * Stealth.getVisibility(player);
            int particles = 64; // number of particles in the ring

            for (int i = 0; i < particles; i++) {
                double angle = 2 * Math.PI * i / particles;
                double x = player.getX() + radius * Math.cos(angle);
                double z = player.getZ() + radius * Math.sin(angle);
                double y = player.getY() + 1;

                player.level().sendParticles(player, ParticleTypes.EXPLOSION, true, true,
                      x, y, z, 1, 0, 0, 0, 0);
            }
        }
    }
}
