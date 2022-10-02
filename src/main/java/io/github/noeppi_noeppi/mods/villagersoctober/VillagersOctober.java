package io.github.noeppi_noeppi.mods.villagersoctober;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.moddingx.libx.mod.ModXRegistration;
import org.moddingx.libx.registration.RegistrationBuilder;

import javax.annotation.Nonnull;

@Mod("villagersoctober")
public final class VillagersOctober extends ModXRegistration {

    private static VillagersOctober instance;
    
    public VillagersOctober() {
        super(new CreativeModeTab("villagersoctober") {
            
            @Nonnull
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(ModBlocks.scarecrow);
            }
        });
        
        instance = this;
    }

    @Nonnull
    public static VillagersOctober getInstance() {
        return instance;
    }

    @Override
    protected void initRegistration(RegistrationBuilder builder) {
        //
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {

    }

    @Override
    protected void clientSetup(FMLClientSetupEvent event) {

    }
}
