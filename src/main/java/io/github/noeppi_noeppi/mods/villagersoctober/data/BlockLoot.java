package io.github.noeppi_noeppi.mods.villagersoctober.data;

import io.github.noeppi_noeppi.mods.villagersoctober.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.loot.BlockLootProviderBase;
import org.moddingx.libx.mod.ModX;

@Datagen
public class BlockLoot extends BlockLootProviderBase {

    public BlockLoot(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected void setup() {
        this.drops(ModBlocks.scarecrow,
                this.stack(Items.HAY_BLOCK),
                this.stack(Items.CARVED_PUMPKIN),
                this.stack(Items.STICK).with(this.count(1, 3)).with(this.random(0.9f))
        );
    }
}
