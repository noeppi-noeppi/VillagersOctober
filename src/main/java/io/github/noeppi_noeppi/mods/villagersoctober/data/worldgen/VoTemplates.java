package io.github.noeppi_noeppi.mods.villagersoctober.data.worldgen;

import io.github.noeppi_noeppi.mods.sandbox.datagen.ext.TemplateData;
import io.github.noeppi_noeppi.mods.sandbox.keys.TemplatePools;
import io.github.noeppi_noeppi.mods.sandbox.template.PoolExtension;
import net.minecraft.core.Holder;

public class VoTemplates extends TemplateData {

    public final Holder<PoolExtension> pumpkinHousePlains = this.extension(TemplatePools.VILLAGE_PLAINS_HOUSES)
            .single(40, "pumpkin_house_plains")
            .build();

    public final Holder<PoolExtension> pumpkinHouseDesert = this.extension(TemplatePools.VILLAGE_DESERT_HOUSES)
            .single(40, "pumpkin_house_desert")
            .build();

    public final Holder<PoolExtension> pumpkinHouseSavanna = this.extension(TemplatePools.VILLAGE_SAVANNA_HOUSES)
            .single(40, "pumpkin_house_savanna")
            .build();

    public final Holder<PoolExtension> pumpkinHouseSnowy = this.extension(TemplatePools.VILLAGE_SNOWY_HOUSES)
            .single(40, "pumpkin_house_snowy")
            .build();

    public final Holder<PoolExtension> pumpkinHouseTaiga = this.extension(TemplatePools.VILLAGE_TAIGA_HOUSES)
            .single(40, "pumpkin_house_taiga")
            .build();
    
    public VoTemplates(Properties properties) {
        super(properties);
    }
}
