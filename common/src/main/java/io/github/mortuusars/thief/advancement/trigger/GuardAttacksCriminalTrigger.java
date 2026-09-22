package io.github.mortuusars.thief.advancement.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
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

    public record TriggerInstance(Optional<Holder<LootItemCondition>> player,
                                  Optional<Holder<LootItemCondition>> guard) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                    LootItemCondition.CODEC.optionalFieldOf("guard").forGetter(TriggerInstance::guard))
                .apply(instance, TriggerInstance::new));

        public boolean matches(ServerPlayer player, LivingEntity guard) {
            return this.guard.isEmpty() || this.guard.get().value().test(EntityPredicate.createContext(player, guard));
        }
    }
}