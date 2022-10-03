package io.github.noeppi_noeppi.mods.villagersoctober.data;

import io.github.noeppi_noeppi.mods.villagersoctober.ModBlocks;
import io.github.noeppi_noeppi.mods.villagersoctober.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.crafting.ingredient.EffectIngredient;
import org.moddingx.libx.datagen.provider.recipe.RecipeProviderBase;
import org.moddingx.libx.datagen.provider.recipe.crafting.CraftingExtension;
import org.moddingx.libx.mod.ModX;

import java.util.List;

@Datagen
public class Recipes extends RecipeProviderBase implements CraftingExtension {

    public Recipes(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @Override
    protected void setup() {
        this.shapeless(ModBlocks.doorbell, Blocks.SMOOTH_STONE, ItemTags.WOODEN_BUTTONS);
        this.shaped(ModBlocks.scarecrow, " p ", "shs", " s ", 'p', Blocks.CARVED_PUMPKIN, 'h', Blocks.HAY_BLOCK, 's', Tags.Items.RODS_WOODEN);

        for (DyeColor color : DyeColor.values()) {
            this.shaped(ModItems.candy.get(color), " s ", "sds", " s ", 's', Items.SUGAR, 'd', color.getTag());
        }
        
        this.shaped(ModItems.witchRobe, "w w", "wpw", "www", 'w', Blocks.PURPLE_WOOL, 'p', new EffectIngredient(Items.POTION, List.of(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 0)), true, true, true));
    }
}
