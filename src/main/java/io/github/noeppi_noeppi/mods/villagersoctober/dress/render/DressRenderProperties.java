package io.github.noeppi_noeppi.mods.villagersoctober.dress.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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
    
    default void renderCustom(RenderContext ctx, PoseStack poseStack, MultiBufferSource buffer, Player player, ItemStack stack, int light, float partialTicks) {
        
    }
    
    interface RenderContext {
        
        void translateTo(PoseStack poseStack, EquipmentSlot slot, boolean left);
        void translateBy(PoseStack poseStack, EquipmentSlot slot, boolean left);
    }
}
