package io.github.noeppi_noeppi.mods.villagersoctober.advancement;

import com.google.gson.JsonObject;
import io.github.noeppi_noeppi.mods.villagersoctober.VillagersOctober;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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
        return new Instance(player, EntityPredicate.Composite.fromJson(json, "entity", context));
    }

    public void trigger(ServerPlayer player, Entity entity) {
        LootContext ctx = EntityPredicate.createContext(player, entity);
        this.trigger(player, instance -> instance.test(ctx));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        public final EntityPredicate.Composite entity;

        public Instance(EntityPredicate.Composite entity) {
            this(EntityPredicate.Composite.ANY, entity);
        }
        
        public Instance(EntityPredicate.Composite player, EntityPredicate.Composite entity) {
            super(HalloweenTrigger.ID, player);
            this.entity = entity;
        }

        public boolean test(LootContext entity) {
            return this.entity.matches(entity);
        }

        @Nonnull
        public JsonObject serializeToJson(@Nonnull SerializationContext conditions) {
            JsonObject json = super.serializeToJson(conditions);
            json.add("entity", this.entity.toJson(conditions));
            return json;
        }
    }
}
