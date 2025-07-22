package io.github.mortuusars.thief.advancement.trigger;

import com.google.gson.JsonObject;
import io.github.mortuusars.thief.Thief;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class GuardAttacksCriminalTrigger extends SimpleCriterionTrigger<GuardAttacksCriminalTrigger.TriggerInstance> {
    public static final ResourceLocation ID = Thief.resource("guard_attacks_criminal");

    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull TriggerInstance createInstance(JsonObject json, @NotNull ContextAwarePredicate predicate,
                                                      @NotNull DeserializationContext deserializationContext) {
        EntityPredicate guard = EntityPredicate.fromJson(json.get("guard"));
        return new TriggerInstance(predicate, guard);
    }

    public void trigger(ServerPlayer player, LivingEntity guard) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(player, guard));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final EntityPredicate guard;

        public TriggerInstance(ContextAwarePredicate predicate, EntityPredicate guard) {
            super(ID, predicate);
            this.guard = guard;
        }

        public boolean matches(ServerPlayer player, LivingEntity guard) {
            return this.guard.equals(EntityPredicate.ANY) || this.guard.matches(player, guard);
        }

        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext conditions) {
            JsonObject jsonobject = super.serializeToJson(conditions);
            jsonobject.add("guard", this.guard.serializeToJson());
            return jsonobject;
        }
    }
}