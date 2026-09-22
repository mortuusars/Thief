package io.github.mortuusars.thief.advancement.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class VillagerGiftTrigger extends SimpleCriterionTrigger<VillagerGiftTrigger.TriggerInstance> {
    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, Villager villager, ItemStack giftStack) {
        this.trigger(player, triggerInstance ->
              triggerInstance.matches(player, villager, giftStack));
    }

    public record TriggerInstance(Optional<Holder<LootItemCondition>> player,
                                  Optional<Holder<LootItemCondition>> entity,
                                  Optional<ItemPredicate> gift) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                    LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entity),
                    ItemPredicate.CODEC.optionalFieldOf("gift").forGetter(TriggerInstance::gift))
              .apply(instance, TriggerInstance::new));

        public boolean matches(ServerPlayer player,
                               Villager villager,
                               ItemStack gift) {
            return (this.entity.isEmpty() || this.entity.get().value().test(EntityPredicate.createContext(player, villager)))
                  && (this.gift.isEmpty() || this.gift.get().test(gift));
        }
    }
}