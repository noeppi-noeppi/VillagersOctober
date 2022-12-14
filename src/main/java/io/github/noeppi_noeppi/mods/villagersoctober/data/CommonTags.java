package io.github.noeppi_noeppi.mods.villagersoctober.data;

import io.github.noeppi_noeppi.mods.villagersoctober.ModItemTags;
import io.github.noeppi_noeppi.mods.villagersoctober.content.CandyItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.CommonTagsProviderBase;
import org.moddingx.libx.mod.ModX;

@Datagen
public class CommonTags extends CommonTagsProviderBase {
    
    public CommonTags(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, fileHelper);
    }

    @Override
    public void setup() {
        this.item(ModItemTags.MYSTICAL_TABLE_ITEMS).add(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION);
        this.item(ModItemTags.GARLAND_ITEMS).add(Items.BONE, Items.APPLE, Items.OXEYE_DAISY, Items.LILY_OF_THE_VALLEY, Items.ROSE_BUSH, Items.EMERALD);
    }

    @Override
    public void defaultItemTags(Item item) {
        if (item instanceof CandyItem) {
            this.item(ModItemTags.CANDY).add(item);
        }
    }
}
