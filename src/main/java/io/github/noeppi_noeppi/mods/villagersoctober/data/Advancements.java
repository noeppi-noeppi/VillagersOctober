package io.github.noeppi_noeppi.mods.villagersoctober.data;

import io.github.noeppi_noeppi.mods.villagersoctober.ModBlocks;
import io.github.noeppi_noeppi.mods.villagersoctober.ModItems;
import io.github.noeppi_noeppi.mods.villagersoctober.advancement.HalloweenTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.AdvancementProviderBase;
import org.moddingx.libx.mod.ModX;

import java.util.Arrays;

@Datagen
public class Advancements extends AdvancementProviderBase {

    public Advancements(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @Override
    public void setup() {
        this.root()
                .display(ModBlocks.scarecrow)
                .background(new ResourceLocation("minecraft", "textures/block/hay_block_side.png"))
                .task(
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(BuiltinStructures.VILLAGE_PLAINS)),
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(BuiltinStructures.VILLAGE_DESERT)),
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(BuiltinStructures.VILLAGE_SAVANNA)),
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(BuiltinStructures.VILLAGE_SNOWY)),
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(BuiltinStructures.VILLAGE_TAIGA))
                );
        
        this.advancement("ghast")
                .display(ModItems.ghastBalloon, FrameType.GOAL)
                .task(KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(ModItems.ghastBalloon.entityType).build(),
                        DamageSourcePredicate.Builder.damageType().isProjectile(true)
                ));
        
        this.advancement("halloween")
                .display(ModItems.candy.get(DyeColor.RED))
                .task(new HalloweenTrigger.Instance(EntityPredicate.Composite.ANY, ItemPredicate.ANY));
        
        this.advancement("candy_collector")
                .parent("halloween")
                .display(ModItems.candy.get(DyeColor.YELLOW))
                .tasks(() -> Arrays.stream(DyeColor.values())
                        .map(ModItems.candy::get)
                        .map(i -> ItemPredicate.Builder.item().of(i).build())
                        .map(i -> new HalloweenTrigger.Instance(EntityPredicate.Composite.ANY, i))
                        .map(t -> new CriterionTriggerInstance[]{t})
                        .toArray(CriterionTriggerInstance[][]::new));
    }
}
