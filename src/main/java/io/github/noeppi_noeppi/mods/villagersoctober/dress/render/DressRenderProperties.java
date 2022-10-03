package io.github.noeppi_noeppi.mods.villagersoctober.dress.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.EquipmentSlot;

import javax.annotation.Nullable;

public interface DressRenderProperties {

    DressRenderProperties EMPTY = new DressRenderProperties() {};

    default RenderType getTargetRenderType() {
        return RenderType.solid();
    }

    @Nullable
    default BakedModel getModel(EquipmentSlot slot) {
        return null;
    }
}
