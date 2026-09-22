package io.github.mortuusars.thief.advancement.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mortuusars.thief.world.Crime;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
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

    public record TriggerInstance(Optional<Holder<LootItemCondition>> player,
                                  Optional<MinMaxBounds.Ints> crime,
                                  Optional<Holder<LootItemCondition>> witness) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                MinMaxBounds.Ints.CODEC.optionalFieldOf("crime").forGetter(TriggerInstance::crime),
                LootItemCondition.CODEC.optionalFieldOf("witness").forGetter(TriggerInstance::witness))
                .apply(instance, TriggerInstance::new));

        public boolean matches(ServerPlayer player, Crime crime, List<LivingEntity> witnesses) {
            return (this.crime.isEmpty() || this.crime.get().matches(crime.ordinal())
                    && (witness.isEmpty() || witnesses.stream().allMatch(w -> witness.get().value().test(EntityPredicate.createContext(player, w)))));
        }
    }
}