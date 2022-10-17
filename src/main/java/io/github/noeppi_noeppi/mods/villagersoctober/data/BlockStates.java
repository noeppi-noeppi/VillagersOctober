package io.github.noeppi_noeppi.mods.villagersoctober.data;

import io.github.noeppi_noeppi.mods.villagersoctober.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.BlockStateProviderBase;
import org.moddingx.libx.mod.ModX;

import java.util.Objects;

@Datagen
public class BlockStates extends BlockStateProviderBase {

    public BlockStates(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, fileHelper);
    }

    @Override
    protected void setup() {
        this.manualState(ModBlocks.doorbell);
        this.manualModel(ModBlocks.mysticalTable);
        this.manualModel(ModBlocks.garland);

        this.makeDoorbell(ModBlocks.doorbell);
        this.makeScarecrow(ModBlocks.scarecrow, ModBlocks.scarecrowTop);
    }
    
    @SuppressWarnings("SameParameterValue")
    private void makeDoorbell(Block block) {
        ResourceLocation id = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block));
        ModelFile base = this.models().getExistingFile(this.mod.resource("block/" + id.getPath()));
        ModelFile pressed = this.models().getExistingFile(this.mod.resource("block/" + id.getPath() + "_pressed"));
        VariantBlockStateBuilder builder = this.getVariantBuilder(block);
        for (Direction direction : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
            builder.partialState()
                    .with(BlockStateProperties.HORIZONTAL_FACING, direction)
                    .with(BlockStateProperties.POWERED, false)
                    .addModels(new ConfiguredModel(base, 0, (int) direction.getOpposite().toYRot(), false));
            
            builder.partialState()
                    .with(BlockStateProperties.HORIZONTAL_FACING, direction)
                    .with(BlockStateProperties.POWERED, true)
                    .addModels(new ConfiguredModel(pressed, 0, (int) direction.getOpposite().toYRot(), false));
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void makeScarecrow(Block block, Block topBlock) {
        for (int i = 0; i <= 5; i++) {
            ResourceLocation tex;
            if (i == 0) {
                tex = new ResourceLocation("minecraft", "block/carved_pumpkin");
            } else {
                tex = this.mod.resource("block/scarecrow_face_" + i);
            }
            ModelFile model = this.models().withExistingParent("scarecrow_" + i, this.mod.resource("block/scarecrow"))
                    .texture("face", tex);
            if (i == 0) this.manualModel(block, model);
        }
        
        ResourceLocation topId = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(topBlock));
        this.manualModel(topBlock, this.models().getBuilder(topId.getPath()).texture("particle", new ResourceLocation("minecraft", "block/hay_block_side")));
    }
}
