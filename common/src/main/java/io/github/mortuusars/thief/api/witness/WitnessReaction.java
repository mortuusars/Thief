package io.github.mortuusars.thief.api.witness;

import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.world.Crime;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.npc.Villager;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows registering custom handlers for witness reactions of a crime.<br>
 * Can be used for entities other than villagers.<br>
 * Use {@link WitnessReaction#register} to register handlers.
 */
public class WitnessReaction {
    private static final List<WitnessReactionHandler> handlers = new ArrayList<>();

    static {
        register((level, crime, witness, criminal) -> {
            if (!(witness instanceof Villager villager)) return false;
            villager.setUnhappy();
            level.onReputationEvent(crime, criminal, villager);
            level.broadcastEntityEvent(villager, EntityEvent.VILLAGER_ANGRY);
            return true;
        });

        register((level, crime, witness, criminal) -> {
            if (witness.getType().is(Thief.Tags.EntityTypes.GUARDS)
                    && witness instanceof NeutralMob neutralMob
                    && crime.shouldGuardsAttack(level, witness, criminal)
                    && witness.canAttack(criminal)
                    && neutralMob.getTarget() == null) { // Only if not already attacking something
                neutralMob.setTarget(criminal); // Attack

                for (int i = 0; i < 5; i++) {
                    double d = witness.getRandom().nextGaussian() * 0.02;
                    double e = witness.getRandom().nextGaussian() * 0.02;
                    double f = witness.getRandom().nextGaussian() * 0.02;
                    level.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                            witness.getRandomX(1.0),
                            witness.getRandomY() + 1.0,
                            witness.getRandomZ(1.0),
                            5, d, e, f, 0.01);
                }

                return true;
            }
            return false;
        });
    }

    public static void register(WitnessReactionHandler handler) {
        handlers.add(handler);
    }

    public static boolean handle(ServerLevel serverLevel, Crime crime, LivingEntity witness, LivingEntity criminal) {
        for (WitnessReactionHandler handler : handlers) {
            boolean result = handler.handle(serverLevel, crime, witness, criminal);
            if (result) {
                return true;
            }
        }
        return false;
    }
}
