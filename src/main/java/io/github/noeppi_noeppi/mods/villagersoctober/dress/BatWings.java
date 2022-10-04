package io.github.noeppi_noeppi.mods.villagersoctober.dress;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.github.noeppi_noeppi.mods.villagersoctober.VillagersOctober;
import io.github.noeppi_noeppi.mods.villagersoctober.dress.render.DressRenderProperties;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.moddingx.libx.annotation.model.Model;
import org.moddingx.libx.mod.ModX;

import javax.annotation.Nullable;

public class BatWings extends DressItem {

    public BatWings(ModX mod, Properties properties) {
        super(mod, EquipmentSlot.CHEST, properties);
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public DressRenderProperties getRenderProperties() {
        return RenderProperties.INSTANCE;
    }

    public static class RenderProperties implements DressRenderProperties {
        
        public static final RenderProperties INSTANCE = new RenderProperties();
        
        public static final ResourceLocation TEXTURE = VillagersOctober.getInstance().resource("textures/dress/bat_wing.png");
        private static final RenderType TYPE = RenderType.entityCutout(TEXTURE);
        
        @Model("dress/bat_wings")
        public static BakedModel model;

        @Nullable
        @Override
        public BakedModel getModel(EquipmentSlot slot) {
            return slot == EquipmentSlot.CHEST ? model : null;
        }

        @Override
        public void renderCustom(RenderContext ctx, PoseStack poseStack, MultiBufferSource buffer, Player player, ItemStack stack, int light, float partialTicks) {
            poseStack.pushPose();
            
            ctx.translateBy(poseStack, EquipmentSlot.CHEST, false);

            poseStack.scale(1/16f, 1/16f, 1/16f);
            poseStack.translate(0, -1, 2);
            
            float angle = 35 + (float) (Math.sin((player.tickCount + partialTicks) * 0.7) * 15);
            
            poseStack.pushPose();
            poseStack.translate(0.4, 0, 0);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(-angle));
            this.blit(poseStack, buffer, 20, 20, light);
            poseStack.popPose();
            
            poseStack.pushPose();
            poseStack.translate(-0.4, 0, 0);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180 + angle));
            this.blit(poseStack, buffer, 20, 20, light);
            poseStack.popPose();
            
            poseStack.popPose();
        }
        
        @SuppressWarnings("SameParameterValue")
        private void blit(PoseStack poseStack, MultiBufferSource buffer, int width, int height, int light) {
            Matrix4f pose = poseStack.last().pose();
            Matrix3f normal = poseStack.last().normal();
            
            VertexConsumer vertex = buffer.getBuffer(TYPE);
            vertex.vertex(pose, 0, height, 0).color(1F, 1f, 1f, 1f).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1f, 0f, 0f).endVertex();
            vertex.vertex(pose, width, height, 0).color(1f, 1f, 1f, 1f).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1f, 0f, 0f).endVertex();
            vertex.vertex(pose, width, 0, 0).color(1f, 1f, 1f, 1f).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1f, 0f, 0f).endVertex();
            vertex.vertex(pose, 0, 0, 0).color(1f, 1f, 1f, 1f).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1f, 0f, 0f).endVertex();

            vertex = buffer.getBuffer(TYPE);
            vertex.vertex(pose, 0, 0, 0).color(1f, 1f, 1f, 1f).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1f, 0f, 0f).endVertex();
            vertex.vertex(pose, width, 0, 0).color(1f, 1f, 1f, 1f).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1f, 0f, 0f).endVertex();
            vertex.vertex(pose, width, height, 0).color(1f, 1f, 1f, 1f).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1f, 0f, 0f).endVertex();
            vertex.vertex(pose, 0, height, 0).color(1F, 1f, 1f, 1f).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1f, 0f, 0f).endVertex();
        }
    }
}
