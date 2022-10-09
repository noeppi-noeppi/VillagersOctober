package io.github.noeppi_noeppi.mods.villagersoctober.core;

import io.github.noeppi_noeppi.mods.villagersoctober.dress.DressItem;
import io.github.noeppi_noeppi.mods.villagersoctober.structure.VillageDecorator;
import io.github.noeppi_noeppi.mods.villagersoctober.villager.VillagerBrainHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

@SuppressWarnings("unused")
public class CoreModUtil {
    
    public static boolean shouldRenderHead(Entity entity) {
        if (!(entity instanceof LivingEntity living)) return true;
        ItemStack stack = living.getItemBySlot(EquipmentSlot.HEAD);
        return !(stack.getItem() instanceof DressItem dress) || dress.usesBlockModel();
    }
    
    public static void afterPlacedTemplate(WorldGenLevel level, StructureTemplate template, StructurePlaceSettings settings, BlockPos at, BlockPos at2) {
        StructureTracker.StructureData data = StructureTracker.get();
        if (data == null || !data.isVillage()) return;
        VillageDecorator.decorate(level, data.structure(), template, settings, at, at2);
    }

    public static Brain.Provider<Villager> wrapVillagerBrain(Brain.Provider<Villager> provider) {
        return VillagerBrainHelper.wrapBrain(provider);
    }

    public static void initVillagerBrain(Villager villager, Brain<Villager> brain) {
        VillagerBrainHelper.initialiseBrain(villager, brain);
    }
}
