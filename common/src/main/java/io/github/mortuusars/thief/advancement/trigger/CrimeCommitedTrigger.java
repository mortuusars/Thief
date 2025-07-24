package io.github.mortuusars.thief.advancement.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mortuusars.thief.world.Crime;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class CrimeCommitedTrigger extends SimpleCriterionTrigger<CrimeCommitedTrigger.TriggerInstance> {
    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, Crime crime, List<LivingEntity> witnesses) {
        this.trigger(player, triggerInstance ->
                triggerInstance.matches(player, crime, witnesses));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                  Optional<MinMaxBounds.Ints> crime,
                                  Optional<ContextAwarePredicate> witness) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                MinMaxBounds.Ints.CODEC.optionalFieldOf("crime").forGetter(TriggerInstance::crime),
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("witness").forGetter(TriggerInstance::witness))
                .apply(instance, TriggerInstance::new));

        public boolean matches(ServerPlayer player, Crime crime, List<LivingEntity> witnesses) {
            return (this.crime.isEmpty() || this.crime.get().matches(crime.ordinal())
                    && (witness.isEmpty() || witnesses.stream().allMatch(w -> witness.get().matches(EntityPredicate.createContext(player, w)))));
        }
    }
}