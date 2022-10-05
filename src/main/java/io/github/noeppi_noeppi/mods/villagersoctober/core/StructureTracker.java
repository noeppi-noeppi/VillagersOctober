package io.github.noeppi_noeppi.mods.villagersoctober.core;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class StructureTracker {
    
    private static final ThreadLocal<StructureData> CURRENT_STRUCTURE = new ThreadLocal<>();
    
    public static void startStructure(StructureStart start, WorldGenLevel level) {
        CURRENT_STRUCTURE.remove();
        RegistryAccess registries = level.registryAccess();
        Structure structure = start.getStructure();
        Registry<Structure> structureRegistry = registries.ownedRegistry(Registry.STRUCTURE_REGISTRY).orElse(null);
        Registry<StructureSet> structureSetRegistry = registries.ownedRegistry(Registry.STRUCTURE_SET_REGISTRY).orElse(null);
        if (structureRegistry == null || structureSetRegistry == null) return;
        ResourceKey<Structure> structureKey = structureRegistry.getResourceKey(structure).orElse(null);
        if (structureKey == null) return;
        StructureSet villages = structureSetRegistry.getOptional(BuiltinStructureSets.VILLAGES).orElse(null);
        boolean isVillage = villages != null && villages.structures().stream().anyMatch(sel -> sel.structure().is(structureKey));
        CURRENT_STRUCTURE.set(new StructureData(structureKey, isVillage));
    }
    
    public static void endStructure() {
        CURRENT_STRUCTURE.remove();
    }
    
    @Nullable
    public static StructureData get() {
        return CURRENT_STRUCTURE.get();
    }
    
    record StructureData(
            ResourceKey<Structure> structure,
            boolean isVillage
    ) {}
}
