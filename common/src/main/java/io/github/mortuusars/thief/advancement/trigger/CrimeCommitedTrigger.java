package io.github.mortuusars.thief.advancement.trigger;

import com.google.gson.JsonObject;
import io.github.mortuusars.thief.Thief;
import io.github.mortuusars.thief.world.Crime;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CrimeCommitedTrigger extends SimpleCriterionTrigger<CrimeCommitedTrigger.TriggerInstance> {
    public static final ResourceLocation ID = Thief.resource("crime_commited");

    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull TriggerInstance createInstance(JsonObject json, @NotNull ContextAwarePredicate predicate,
                                                      @NotNull DeserializationContext deserializationContext) {
        MinMaxBounds.Ints crime = MinMaxBounds.Ints.fromJson(json.get("crime"));
        EntityPredicate witness = EntityPredicate.fromJson(json.get("witness"));
        return new TriggerInstance(predicate, crime, witness);
    }

    public void trigger(ServerPlayer player, Crime crime, List<LivingEntity> witnesses) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(player, crime, witnesses));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final MinMaxBounds.Ints crime;
        private final EntityPredicate witness;

        public TriggerInstance(ContextAwarePredicate predicate, MinMaxBounds.Ints crime, EntityPredicate witness) {
            super(ID, predicate);
            this.crime = crime;
            this.witness = witness;
        }

        public boolean matches(ServerPlayer player, Crime crime, List<LivingEntity> witnesses) {
            return (this.crime.isAny() || this.crime.matches(crime.ordinal())
                    && (witness.equals(EntityPredicate.ANY) || witnesses.stream().allMatch(w -> witness.matches(player, w))));
        }

        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext conditions) {
            JsonObject jsonobject = super.serializeToJson(conditions);
            jsonobject.add("crime", this.crime.serializeToJson());
            jsonobject.add("witness", this.witness.serializeToJson());
            return jsonobject;
        }
    }
}