package io.github.noeppi_noeppi.mods.villagersoctober.data.worldgen;

import io.github.noeppi_noeppi.mods.sandbox.datagen.WorldGenProviderBase;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.mod.ModX;

@Datagen
public class WorldGenData extends WorldGenProviderBase {

    public WorldGenData(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, fileHelper);
    }

    @Override
    protected void setup() {
        this.addData(VoTemplates::new);
    }
}
