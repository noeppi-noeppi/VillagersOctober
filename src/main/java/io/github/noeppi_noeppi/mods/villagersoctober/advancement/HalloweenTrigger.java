package io.github.noeppi_noeppi.mods.villagersoctober.advancement;

import com.google.gson.JsonObject;
import io.github.noeppi_noeppi.mods.villagersoctober.VillagersOctober;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import javax.annotation.Nonnull;

public class HalloweenTrigger extends SimpleCriterionTrigger<HalloweenTrigger.Instance> {
    
    public static final ResourceLocation ID = VillagersOctober.getInstance().resource("halloween");

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Nonnull
    @Override
    protected Instance createInstance(@Nonnull JsonObject json, @Nonnull EntityPredicate.Composite player, @Nonnull DeserializationContext context) {
        return new Instance(player, EntityPredicate.Composite.fromJson(json, "entity", context), json.has("item") ? ItemPredicate.fromJson(json.get("item")) : ItemPredicate.ANY);
    }

    public void trigger(ServerPlayer player, Entity entity, ItemStack stack) {
        LootContext ctx = EntityPredicate.createContext(player, entity);
        this.trigger(player, instance -> instance.test(ctx, stack));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        public final EntityPredicate.Composite entity;
        public final ItemPredicate item;

        public Instance(EntityPredicate.Composite entity, ItemPredicate item) {
            this(EntityPredicate.Composite.ANY, entity, item);
        }
        
        public Instance(EntityPredicate.Composite player, EntityPredicate.Composite entity, ItemPredicate item) {
            super(HalloweenTrigger.ID, player);
            this.entity = entity;
            this.item = item;
        }

        public boolean test(LootContext entity, ItemStack stack) {
            return this.entity.matches(entity) && this.item.matches(stack);
        }

        @Nonnull
        public JsonObject serializeToJson(@Nonnull SerializationContext conditions) {
            JsonObject json = super.serializeToJson(conditions);
            json.add("entity", this.entity.toJson(conditions));
            json.add("item", this.item.serializeToJson());
            return json;
        }
    }
}
