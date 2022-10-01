package io.github.noeppi_noeppi.mods.villagersoctober.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.SoundDefinitionProviderBase;
import org.moddingx.libx.mod.ModX;

@Datagen
public class Sounds extends SoundDefinitionProviderBase {

    public Sounds(ModX mod, DataGenerator generator, ExistingFileHelper helper) {
        super(mod, generator, helper);
    }

    @Override
    protected void setup() {
        
    }
}
