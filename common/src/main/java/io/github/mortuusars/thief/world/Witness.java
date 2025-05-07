package io.github.mortuusars.thief.world;

import io.github.mortuusars.thief.Config;
import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.world.stealth.Stealth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.Collections;
import java.util.List;

public class Witness {
    public static List<LivingEntity> getWitnesses(LivingEntity criminal) {
        if (criminal instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return Collections.emptyList();
        }

        double visibility = Stealth.getVisibility(criminal);

        int radius = Config.Server.WITNESS_MAX_DISTANCE.get();
        AABB crimeScene = new AABB(criminal.blockPosition()).inflate(radius, radius * 0.5f, radius);

        return criminal.level().getEntitiesOfClass(LivingEntity.class, crimeScene)
                .stream()
                .filter(e -> isWitness(criminal, e, visibility))
                .toList();
    }

    public static <T extends LivingEntity> List<T> getWitnesses(LivingEntity criminal, Class<T> entityClass) {
        if (criminal instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return Collections.emptyList();
        }

        double visibility = Stealth.getVisibility(criminal);

        int radius = Config.Server.WITNESS_MAX_DISTANCE.get();
        AABB crimeScene = new AABB(criminal.blockPosition()).inflate(radius, radius * 0.5f, radius);

        return criminal.level().getEntitiesOfClass(entityClass, crimeScene)
                .stream()
                .filter(e -> isWitness(criminal, e, visibility))
                .toList();
    }

    public static boolean isWitness(LivingEntity criminal, LivingEntity entity, double visibility) {
        if (!entity.getType().is(Thief.Tags.EntityTypes.WITNESSES)) return false;
        float distance = entity.distanceTo(criminal);
        if (distance <= Config.Server.WITNESS_ALWAYS_NOTICE_DISTANCE.get() / 2.0) return true; // Too close. Always hears or sees the crime.
        if (entity.isSleeping()) return false; // Cannot hear the crime.
        if (distance <= Config.Server.WITNESS_ALWAYS_NOTICE_DISTANCE.get()) return true; // Hears or sees if not sleeping.
        if (distance > Config.Server.WITNESS_MAX_DISTANCE.get() * visibility) return false;
        return entity.hasLineOfSight(criminal);
    }
}
