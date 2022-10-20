package io.github.noeppi_noeppi.mods.villagersoctober.bloon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.phys.Vec3;
import org.moddingx.libx.render.RenderHelper;

import javax.annotation.Nonnull;

public class GhastBalloonRenderer extends EntityRenderer<GhastBalloon> {

    public static final float GHAST_SCALE = 0.33f;
    
    private ClientLevel level;
    private Ghast ghast;
    private EntityRenderer<Ghast> renderer;
    
    protected GhastBalloonRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0;
        this.shadowStrength = 0;
    }

    private boolean updateState() {
        if (Minecraft.getInstance().level == null) return false;
        if (this.level == null || this.level != Minecraft.getInstance().level || ghast == null || renderer == null) {
            this.level = Minecraft.getInstance().level;
            this.ghast = new Ghast(EntityType.GHAST, this.level);
            //noinspection unchecked
            this.renderer = (EntityRenderer<Ghast>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(this.ghast);
            //noinspection ConstantConditions
            return this.renderer != null;
        }
        return true;
    }
    
    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull GhastBalloon entity) {
        return RenderHelper.TEXTURE_WHITE;
    }

    @Override
    public void render(@Nonnull GhastBalloon balloon, float entityYaw, float partialTick, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light) {
        super.render(balloon, entityYaw, partialTick, poseStack, buffer, light);
        if (this.updateState()) {
            poseStack.pushPose();
            poseStack.scale(GHAST_SCALE, GHAST_SCALE, GHAST_SCALE);
            this.ghast.setPos(balloon.position());
            this.ghast.xo = balloon.xo;
            this.ghast.yo = balloon.yo;
            this.ghast.zo = balloon.zo;
            this.ghast.xOld = balloon.xOld;
            this.ghast.yOld = balloon.yOld;
            this.ghast.zOld = balloon.zOld;
            this.ghast.setXRot(balloon.getXRot());
            this.ghast.setYRot(balloon.getYRot());
            this.ghast.xRotO = balloon.xRotO;
            this.ghast.yRotO = balloon.yRotO;
            this.ghast.yBodyRot = balloon.getYRot();
            this.ghast.yBodyRotO = balloon.yRotO;
            this.ghast.tickCount = balloon.tickCount;
            this.renderer.render(this.ghast, entityYaw, partialTick, poseStack, buffer, light);
            poseStack.popPose();
            
            this.renderLeash(balloon, partialTick, poseStack, buffer, light);
        }
    }

    private void renderLeash(GhastBalloon balloon, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        Vec3 leashTieVec = balloon.getTieVec();
        if (leashTieVec == null) return;

        poseStack.pushPose();
        
        double balloonRot = Mth.lerp(partialTicks, balloon.yRotO, balloon.getYRot()) * (Math.PI / 180) + (Math.PI / 2);
        Vec3 leashOffset = this.ghast.getLeashOffset().multiply(GHAST_SCALE, GHAST_SCALE, GHAST_SCALE);
        
        double offX = Math.cos(balloonRot) * leashOffset.z + Math.sin(balloonRot) * leashOffset.x;
        double offZ = Math.sin(balloonRot) * leashOffset.z - Math.cos(balloonRot) * leashOffset.x;
        double dx = Mth.lerp(partialTicks, balloon.xo, balloon.getX()) + offX;
        double dy = Mth.lerp(partialTicks, balloon.yo, balloon.getY()) + leashOffset.y;
        double dz = Mth.lerp(partialTicks, balloon.zo, balloon.getZ()) + offZ;
        
        poseStack.translate(offX, leashOffset.y, offZ);
        
        float fx = (float) (leashTieVec.x - dx);
        float fy = (float) (leashTieVec.y - dy);
        float fz = (float) (leashTieVec.z - dz);

        VertexConsumer vertex = buffer.getBuffer(RenderType.leash());
        Matrix4f pose = poseStack.last().pose();
        
        float normFactor = Mth.fastInvSqrt(fx * fx + fz * fz) * 0.025F / 2.0F;
        float nx = fx * normFactor;
        float nz = fz * normFactor;

        for(int i1 = 0; i1 <= 24; ++i1) {
            addVertexPair(vertex, pose, fx, fy, fz, light, nx, nz, i1, false);
        }

        for(int j1 = 24; j1 >= 0; --j1) {
            addVertexPair(vertex, pose, fx, fy, fz, light, nx, nz, j1, true);
        }

        poseStack.popPose();
    }

   private static void addVertexPair(VertexConsumer vertex, Matrix4f pose, float x, float y, float z, int light, float nx, float nz, int idx, boolean reverse) {
      float amount = idx / 24f;
      float shade = idx % 2 == (reverse ? 1 : 0) ? 0.7f : 1;
      float r = 0.5f * shade;
      float g = 0.4f * shade;
      float b = 0.3f * shade;
      float xb = x * amount;
      float yb = y > 0 ? y * amount * amount : y - y * (1 - amount) * (1 - amount);
      float zb = z * amount;
      vertex.vertex(pose, xb - nz, yb + (reverse ? 0 : 0.025f), zb + nx).color(r, g, b, 1.0F).uv2(light).endVertex();
      vertex.vertex(pose, xb + nz, yb + (reverse ? 0.025f : 0), zb - nx).color(r, g, b, 1.0F).uv2(light).endVertex();
   }
}
