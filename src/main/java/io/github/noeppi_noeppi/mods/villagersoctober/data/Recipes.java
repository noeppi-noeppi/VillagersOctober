package io.github.noeppi_noeppi.mods.villagersoctober.data;

import io.github.noeppi_noeppi.mods.villagersoctober.ModBlocks;
import io.github.noeppi_noeppi.mods.villagersoctober.ModItemTags;
import io.github.noeppi_noeppi.mods.villagersoctober.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.recipe.RecipeProviderBase;
import org.moddingx.libx.datagen.provider.recipe.crafting.CraftingExtension;
import org.moddingx.libx.mod.ModX;

@Datagen
public class Recipes extends RecipeProviderBase implements CraftingExtension {

    public Recipes(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @Override
    protected void setup() {
        this.shapeless(ModBlocks.doorbell, Blocks.SMOOTH_STONE, ItemTags.WOODEN_BUTTONS);
        this.shaped(ModBlocks.scarecrow, " p ", "shs", " s ", 'p', Blocks.CARVED_PUMPKIN, 'h', Blocks.HAY_BLOCK, 's', Tags.Items.RODS_WOODEN);
        this.shaped(ModBlocks.mysticalTable, "ccc", "p p", "p p", 'c', Blocks.GREEN_TERRACOTTA, 'p', ItemTags.PLANKS);
        this.shaped(ModBlocks.garland, "sss", "cic", 's', Items.STRING, 'c', ModItemTags.CANDY, 'i', ModItemTags.GARLAND_ITEMS);

        for (DyeColor color : DyeColor.values()) {
            this.shaped(ModItems.candy.get(color), " s ", "sds", " s ", 's', Items.SUGAR, 'd', color.getTag());
        }
        
        this.shaped(ModItems.witchHat, " b ", "bsb", "bgb", 'b', Blocks.BLACK_WOOL, 's', Items.SPIDER_EYE, 'g', Blocks.LIME_WOOL);
        this.shaped(ModItems.witchRobe, "p p", "pgp", "pgp", 'p', Blocks.PURPLE_WOOL, 'g', Blocks.GREEN_WOOL);
        this.shaped(ModItems.batMask, "w w", "www", "f f", 'w', Blocks.BROWN_WOOL, 'f', Items.RABBIT_HIDE);
        this.shaped(ModItems.batWings, "l l", "lsl", "l l", 'l', Items.LEATHER, 's', Items.STRING);
        this.shaped(ModItems.vampireCloak, "rtr", "ggg", "g g", 'r', Blocks.RED_WOOL, 'g', Blocks.GRAY_WOOL, 't', Blocks.REDSTONE_TORCH);
        this.shaped(ModItems.vampireSuit, "rrr", "r r", "b b", 'r', Blocks.RED_WOOL, 'b', Blocks.BLACK_WOOL);
    }
}
