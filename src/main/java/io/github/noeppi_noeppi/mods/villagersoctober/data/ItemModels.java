package io.github.noeppi_noeppi.mods.villagersoctober.data;

import io.github.noeppi_noeppi.mods.villagersoctober.village.DoorBell;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.ItemModelProviderBase;
import org.moddingx.libx.mod.ModX;

@Datagen
public class ItemModels extends ItemModelProviderBase {

    public ItemModels(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, fileHelper);
    }

    @Override
    protected void setup() {
        
    }

    @Override
    protected void defaultBlock(ResourceLocation id, BlockItem item) {
        if (item.getBlock() instanceof DoorBell) {
            this.withExistingParent(id.getPath(), GENERATED).texture("layer0", new ResourceLocation(id.getNamespace(), "item/" + id.getPath()));
        } else {
            super.defaultBlock(id, item);
        }
    }
}
