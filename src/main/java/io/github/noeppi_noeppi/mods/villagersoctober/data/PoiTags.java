package io.github.noeppi_noeppi.mods.villagersoctober.data;

import io.github.noeppi_noeppi.mods.villagersoctober.villager.profession.ModPoiTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.TagProviderBase;
import org.moddingx.libx.mod.ModX;

@Datagen
public class PoiTags extends TagProviderBase<PoiType> {

    public PoiTags(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, ForgeRegistries.POI_TYPES, fileHelper);
    }

    @Override
    protected void setup() {
        this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(ModPoiTypes.mysticalTable);
    }
}
