package io.github.noeppi_noeppi.mods.villagersoctober.villager.profession;

import io.github.noeppi_noeppi.mods.villagersoctober.ModBlocks;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import org.moddingx.libx.annotation.registration.RegisterClass;

import java.util.Set;

@RegisterClass(registry = "POINT_OF_INTEREST_TYPE_REGISTRY")
public class ModPoiTypes {
    
    public static final PoiType mysticalTable = new PoiType(Set.copyOf(ModBlocks.mysticalTable.getStateDefinition().getPossibleStates()), 1, 1);
}
