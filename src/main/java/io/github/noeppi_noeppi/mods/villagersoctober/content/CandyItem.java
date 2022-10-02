package io.github.noeppi_noeppi.mods.villagersoctober.content;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.moddingx.libx.base.ItemBase;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.Registerable;

import javax.annotation.Nonnull;

public class CandyItem extends ItemBase implements Registerable {
    
    public final DyeColor color;
    
    public CandyItem(ModX mod, DyeColor color) {
        super(mod, new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0).fast().alwaysEat().build()));
        this.color = color;
    }

    @Nonnull
    @Override
    protected String getOrCreateDescriptionId() {
        return "item." + this.mod.modid + ".candy";
    }

    @Nonnull
    @Override
    public Component getDescription() {
        return Component.translatable(this.getDescriptionId()).withStyle(Style.EMPTY.withColor(this.color.getFireworkColor() & 0xFFFFFF));
    }

    @Nonnull
    @Override
    public Component getName(@Nonnull ItemStack stack) {
        return Component.translatable(this.getDescriptionId(stack)).withStyle(Style.EMPTY.withColor(this.color.getFireworkColor() & 0xFFFFFF));
    }
}
