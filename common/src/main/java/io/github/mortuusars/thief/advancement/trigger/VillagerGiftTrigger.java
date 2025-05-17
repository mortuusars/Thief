package io.github.mortuusars.thief.advancement.trigger;

import com.google.gson.JsonObject;
import io.github.mortuusars.thief.Thief;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class VillagerGiftTrigger extends SimpleCriterionTrigger<VillagerGiftTrigger.TriggerInstance> {
    public static final ResourceLocation ID = Thief.resource("villager_gift");

    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull TriggerInstance createInstance(JsonObject json, @NotNull ContextAwarePredicate predicate,
                                                      @NotNull DeserializationContext deserializationContext) {
        EntityPredicate entityPredicate = EntityPredicate.fromJson(json.get("entity"));
        ItemPredicate itemPredicate = ItemPredicate.fromJson(json.get("gift"));
        return new TriggerInstance(predicate, entityPredicate, itemPredicate);
    }

    public void trigger(ServerPlayer player, LivingEntity entity, ItemStack gift) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(player, entity, gift));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final EntityPredicate entityPredicate;
        private final ItemPredicate itemPredicate;

        public TriggerInstance(ContextAwarePredicate predicate, EntityPredicate entityPredicate, ItemPredicate itemPredicate) {
            super(ID, predicate);
            this.entityPredicate = entityPredicate;
            this.itemPredicate = itemPredicate;
        }

        public boolean matches(ServerPlayer player, LivingEntity entity, ItemStack gift) {
            return (this.entityPredicate.equals(EntityPredicate.ANY) || this.entityPredicate.matches(player, entity))
                    && (this.itemPredicate.equals(ItemPredicate.ANY) || this.itemPredicate.matches(gift));
        }

        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext conditions) {
            JsonObject jsonobject = super.serializeToJson(conditions);
            jsonobject.add("entity", this.entityPredicate.serializeToJson());
            jsonobject.add("gift", this.itemPredicate.serializeToJson());
            return jsonobject;
        }
    }
}