package io.github.noeppi_noeppi.mods.villagersoctober.data;

import io.github.noeppi_noeppi.mods.villagersoctober.content.CandyItem;
import io.github.noeppi_noeppi.mods.villagersoctober.dress.DressItem;
import io.github.noeppi_noeppi.mods.villagersoctober.garland.GarlandBlock;
import io.github.noeppi_noeppi.mods.villagersoctober.scarecrow.ScarecrowBlock;
import io.github.noeppi_noeppi.mods.villagersoctober.bell.DoorBellBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;
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
    protected void defaultItem(ResourceLocation id, Item item) {
        if (item instanceof DressItem dress && dress.usesBlockModel()) {
            this.withExistingParent(id.getPath(), this.mod.resource("dress/" + id.getPath()));
        } else if (item instanceof CandyItem) {
            this.withExistingParent(id.getPath(), GENERATED).texture("layer0", this.mod.resource("item/candy"));
        } else {
            super.defaultItem(id, item);
        }
    }

    @Override
    protected void defaultBlock(ResourceLocation id, BlockItem item) {
        if (item.getBlock() instanceof DoorBellBlock || item.getBlock() instanceof GarlandBlock) {
            this.withExistingParent(id.getPath(), GENERATED).texture("layer0", new ResourceLocation(id.getNamespace(), "item/" + id.getPath()));
        } else if (item.getBlock() instanceof ScarecrowBlock) {
            this.getBuilder(id.getPath()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_0")));
        } else {
            super.defaultBlock(id, item);
        }
    }
}
