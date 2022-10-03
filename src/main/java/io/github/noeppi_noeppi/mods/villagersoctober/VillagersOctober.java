package io.github.noeppi_noeppi.mods.villagersoctober;

import io.github.noeppi_noeppi.mods.villagersoctober.dress.render.DressLayer;
import io.github.noeppi_noeppi.mods.villagersoctober.util.RenderLayerHelper;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initColors);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::reloadClientResources);
        });
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

    private void initColors(RegisterColorHandlersEvent.Item event) {
        for (DyeColor color : DyeColor.values()) {
            event.register((stack, tintIndex) -> color.getFireworkColor() & 0xFFFFFF, ModItems.candy.get(color));
        }
    }
    
    private void reloadClientResources(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new SimplePreparableReloadListener<Void>() {

            @Nonnull
            @Override
            protected Void prepare(@Nonnull ResourceManager rm, @Nonnull ProfilerFiller filler) {
                return null;
            }

            @Override
            protected void apply(@Nonnull Void value, @Nonnull ResourceManager rm, @Nonnull ProfilerFiller filler) {
                RenderLayerHelper.addPlayerLayer(DressLayer::new);
            }
        });
    }
}
