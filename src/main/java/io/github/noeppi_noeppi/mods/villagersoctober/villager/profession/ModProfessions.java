package io.github.noeppi_noeppi.mods.villagersoctober.villager.profession;

import com.google.common.collect.ImmutableSet;
import io.github.noeppi_noeppi.mods.villagersoctober.VillagersOctober;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.ForgeRegistries;
import org.moddingx.libx.annotation.registration.RegisterClass;

@RegisterClass(registry = "VILLAGER_PROFESSION_REGISTRY")
public class ModProfessions {
    
    public static final VillagerProfession madman = new VillagerProfession(
            VillagersOctober.getInstance().resource("madman").toString(),
            h -> ForgeRegistries.POI_TYPES.getResourceKey(ModPoiTypes.mysticalTable).filter(h::is).isPresent(),
            h -> ForgeRegistries.POI_TYPES.getResourceKey(ModPoiTypes.mysticalTable).filter(h::is).isPresent(),
            ImmutableSet.of(), ImmutableSet.of(),
            SoundEvents.BAT_HURT
    );
}
