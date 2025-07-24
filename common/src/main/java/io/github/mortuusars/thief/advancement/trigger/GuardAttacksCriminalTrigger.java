package io.github.mortuusars.thief.advancement.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class GuardAttacksCriminalTrigger extends SimpleCriterionTrigger<GuardAttacksCriminalTrigger.TriggerInstance> {
    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, LivingEntity guard) {
        this.trigger(player, triggerInstance ->
                triggerInstance.matches(player, guard));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                  Optional<ContextAwarePredicate> guard) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("guard").forGetter(TriggerInstance::guard))
                .apply(instance, TriggerInstance::new));

        public boolean matches(ServerPlayer player, LivingEntity guard) {
            return this.guard.isEmpty() || this.guard.get().matches(EntityPredicate.createContext(player, guard));
        }
    }
}