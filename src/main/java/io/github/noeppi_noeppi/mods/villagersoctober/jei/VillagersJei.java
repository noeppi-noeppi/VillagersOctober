package io.github.noeppi_noeppi.mods.villagersoctober.jei;

import io.github.noeppi_noeppi.mods.villagersoctober.ModBlocks;
import io.github.noeppi_noeppi.mods.villagersoctober.ModItems;
import io.github.noeppi_noeppi.mods.villagersoctober.VillagersOctober;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.moddingx.libx.util.Misc;
import org.moddingx.libx.util.game.TextProcessor;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JeiPlugin
public class VillagersJei implements IModPlugin {

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return VillagersOctober.getInstance().resource("jeiplugin");
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        addItemDescription(registration, "candy", Arrays.stream(DyeColor.values()).map(ModItems.candy::get).toList());
        addItemDescription(registration, ModBlocks.mysticalTable);
        addItemDescription(registration, ModItems.ghastBalloon);
    }
    
    private void addItemDescription(IRecipeRegistration registration, ItemLike item) {
        this.addItemDescription(registration, Objects.requireNonNullElse(ForgeRegistries.ITEMS.getKey(item.asItem()), Misc.MISSIGNO), List.of(item));
    }
    
    @SuppressWarnings("SameParameterValue")
    private void addItemDescription(IRecipeRegistration registration, String id, List<? extends ItemLike> items) {
        this.addItemDescription(registration, VillagersOctober.getInstance().resource(id), items);
    }
    
    private void addItemDescription(IRecipeRegistration registration, ResourceLocation id, List<? extends ItemLike> items) {
        String key = "jei." + id.getNamespace() + "." + id.getPath();
        registration.addItemStackInfo(items.stream().map(ItemLike::asItem).map(ItemStack::new).toList(), TextProcessor.INSTANCE.process(Component.translatable(key)).toArray(Component[]::new));
    }
}
