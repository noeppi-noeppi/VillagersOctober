package io.github.noeppi_noeppi.mods.villagersoctober.dress.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.noeppi_noeppi.mods.villagersoctober.dress.DressItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class DressLayer<M extends HumanoidModel<Player>> extends RenderLayer<Player, M> {

    private final EquipmentSlot[] RENDER_SLOTS = new EquipmentSlot[]{
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET,
            EquipmentSlot.MAINHAND
    };

    private final DressRenderProperties.RenderContext context = new DressRenderProperties.RenderContext() {

        @Override
        public void translateBy(PoseStack poseStack, EquipmentSlot slot, boolean left) {
            DressLayer.this.translateBy(poseStack, slot, left);
        }

        @Override
        public void translateTo(PoseStack poseStack, EquipmentSlot slot, boolean left) {
            DressLayer.this.translateTo(poseStack, slot, left);
        }
    };
    
    public DressLayer(RenderLayerParent<Player, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, @Nonnull Player entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        renderItem(poseStack, buffer, entity, light, EquipmentSlot.HEAD, partialTick);
        renderItem(poseStack, buffer, entity, light, EquipmentSlot.CHEST, partialTick);
        renderItem(poseStack, buffer, entity, light, EquipmentSlot.LEGS, partialTick);
        renderItem(poseStack, buffer, entity, light, EquipmentSlot.FEET, partialTick);
    }
    
    private void renderItem(PoseStack poseStack, MultiBufferSource buffer, Player entity, int light, EquipmentSlot slot, float partialTicks) {
        ItemStack stack = entity.getItemBySlot(slot);
        if (!stack.isEmpty() && stack.getItem() instanceof DressItem dress && slot == dress.slot) {
            DressRenderProperties properties = dress.getRenderProperties();
            for (EquipmentSlot renderSlot : RENDER_SLOTS) {
                BakedModel model = properties.getModel(renderSlot);
                if (model != null) {
                    this.renderModel(poseStack, buffer, stack, model, properties.getTargetRenderType(), renderSlot, light, false);
                    if (renderSlot == EquipmentSlot.LEGS || renderSlot == EquipmentSlot.FEET || renderSlot == EquipmentSlot.MAINHAND) {
                        this.renderModel(poseStack, buffer, stack, model, properties.getTargetRenderType(), renderSlot, light, true);
                    }
                }
            }
            properties.renderCustom(this.context, poseStack, buffer, entity, stack, light, partialTicks);
        }
    }
    
    private void renderModel(PoseStack poseStack, MultiBufferSource buffer, ItemStack stack, BakedModel model, RenderType renderType, EquipmentSlot slot, int light, boolean left) {
        poseStack.pushPose();
        this.translateTo(poseStack, slot, left);
        model.applyTransform(ItemTransforms.TransformType.HEAD, poseStack, left);
        poseStack.translate(-0.5, -0.5, -0.5);
        VertexConsumer vertex = buffer.getBuffer(renderType);
        Minecraft.getInstance().getItemRenderer().renderModelLists(model, stack, light, OverlayTexture.NO_OVERLAY, poseStack, vertex);
        poseStack.popPose();
    }
    
    private void translateBy(PoseStack poseStack, EquipmentSlot slot, boolean left) {
        switch (slot) {
            case HEAD -> this.getParentModel().head.translateAndRotate(poseStack);
            case CHEST -> this.getParentModel().body.translateAndRotate(poseStack);
            case LEGS, FEET -> {
                if (left) {
                    this.getParentModel().leftLeg.translateAndRotate(poseStack);
                    poseStack.translate(-1.9/16d, -12/16d, 0);
                } else {
                    this.getParentModel().rightLeg.translateAndRotate(poseStack);
                    poseStack.translate(1.9/16d, -12/16d, 0);
                }
            }
            case MAINHAND -> {
                if (left) {
                    this.getParentModel().leftArm.translateAndRotate(poseStack);
                    poseStack.translate(-5/16d, -2/16d, 0);
                } else {
                    this.getParentModel().rightArm.translateAndRotate(poseStack);
                    poseStack.translate(5/16d, -2/16d, 0);
                }
            }
        }
    }

    private void translateTo(PoseStack poseStack, EquipmentSlot slot, boolean left) {
        this.translateBy(poseStack, slot, left);
        poseStack.translate(0, -0.25, 0);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        poseStack.scale(0.625f, -0.625f, -0.625f);
    }
}
