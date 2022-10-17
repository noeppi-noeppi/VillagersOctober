package io.github.noeppi_noeppi.mods.villagersoctober.dress;

import io.github.noeppi_noeppi.mods.villagersoctober.dress.render.DressRenderProperties;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.moddingx.libx.annotation.model.Model;
import org.moddingx.libx.mod.ModX;

import javax.annotation.Nullable;

public class VampireSuit extends DressItem {

    public VampireSuit(ModX mod, Properties properties) {
        super(mod, EquipmentSlot.LEGS, properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public DressRenderProperties getRenderProperties() {
        return RenderProperties.INSTANCE;
    }

    public static class RenderProperties implements DressRenderProperties {

        public static final RenderProperties INSTANCE = new RenderProperties();

        @Model("dress/vampire_suit")
        public static BakedModel mainModel;
        
        @Model("dress/vampire_suit_leg")
        public static BakedModel legModel;

        @Override
        public RenderType getTargetRenderType() {
            return RenderType.cutout();
        }

        @Nullable
        @Override
        public BakedModel getModel(EquipmentSlot slot) {
            if (slot == EquipmentSlot.CHEST) {
                return mainModel;
            } else if (slot == EquipmentSlot.LEGS) {
                return legModel;
            } else {
                return null;
            }
        }
    }
}
