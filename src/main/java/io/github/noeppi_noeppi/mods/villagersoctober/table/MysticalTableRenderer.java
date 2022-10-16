package io.github.noeppi_noeppi.mods.villagersoctober.table;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import org.moddingx.libx.inventory.BaseItemStackHandler;
import org.moddingx.libx.render.block.RotatedBlockRenderer;

import javax.annotation.Nonnull;

public class MysticalTableRenderer extends RotatedBlockRenderer<MysticalTable> {

    @Override
    protected void doRender(@Nonnull MysticalTable table, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        BaseItemStackHandler inventory = table.getInventory();
        int slot = 0;
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            poseStack.pushPose();
            if (transformSlot(poseStack, slot)) {
                Minecraft.getInstance().getItemRenderer().renderStatic(
                        stack, ItemTransforms.TransformType.GROUND,
                        light, OverlayTexture.NO_OVERLAY,
                        poseStack, buffer, 0
                );
            }
            poseStack.popPose();
            slot += 1;
        }
    }
    
    private boolean transformSlot(PoseStack poseStack, int slot) {
        return switch (slot) {
            case 0 -> {
                poseStack.translate(0.5, 0.65, 0.6);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
                yield true;
            }
            case 1 -> {
                poseStack.translate(0.7, 0.7, 0.35);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(200));
                poseStack.mulPose(Vector3f.XP.rotationDegrees(100));
                yield true;
            }
            case 2 -> {
                poseStack.translate(0.3, 0.7, 0.35);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(160));
                poseStack.mulPose(Vector3f.XP.rotationDegrees(100));
                yield true;
            }
            case 3 -> {
                poseStack.translate(0.7, 0.7, 0.85);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(200));
                poseStack.mulPose(Vector3f.XP.rotationDegrees(80));
                yield true;
            }
            case 4 -> {
                poseStack.translate(0.3, 0.7, 0.85);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(160));
                poseStack.mulPose(Vector3f.XP.rotationDegrees(80));
                yield true;
            }
            default -> false;
        };
    }
}
