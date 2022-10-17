package io.github.noeppi_noeppi.mods.villagersoctober.garland;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.noeppi_noeppi.mods.villagersoctober.ModItemTags;
import io.github.noeppi_noeppi.mods.villagersoctober.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.moddingx.libx.render.RenderHelper;
import org.moddingx.libx.util.data.TagAccess;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Random;

public class GarlandRenderer implements BlockEntityRenderer<Garland> {

    // Minimum an maximum absolute values for the largest/smallest value used in the cosh function
    // linearly interpolated between min an max size.
    public static final double MIN_DIST_F = 1;
    public static final double MAX_DIST_F = 2;
    
    // Factor for the cosh function
    public static final double HEIGHT_FACTOR = 1;
    public static final double GARLAND_RADIUS = 0.1;
    public static final double ITEMS_PER_BLOCK = 0.5;
    
    private static final RenderType TARGET = RenderType.entitySolid(RenderHelper.TEXTURE_WHITE);
    private static final double SQRT_HALF = Math.sqrt(0.5);
    private static final Vec3 VEC_UP = new Vec3(0, 1, 0);
    
    @Override
    public void render(@Nonnull Garland garland, float partialTick, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        if (!garland.primary() || garland.getLevel() == null) return;

        Random random = new Random(Mth.getSeed(garland.getBlockPos()));
        int rgb = Color.HSBtoRGB(random.nextFloat(360), 0.6f, 0.6f);
        float r = ((rgb >>> 16) & 0xFF) / 255f;
        float g = ((rgb >>> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;
        
        Level level = garland.getLevel();
        Vec3 basePos = new Vec3(garland.getBlockPos().getX(), garland.getBlockPos().getY(), garland.getBlockPos().getZ());
        Vec3 from = getHangingPoint(garland.getBlockState(), garland.getBlockPos());
        Vec3 to = getHangingPoint(level.getBlockState(garland.otherPos()), garland.otherPos());
        double distance = from.distanceTo(to);
        int steps = (int) Math.round(distance * 2);
        
        Vec3 last = from.subtract(basePos);
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer vertex = buffer.getBuffer(TARGET);
        Vec3[] offsetNormals = getOffsetNormals(from, to);
        for (int i = 0; i < steps; i++) {
            Vec3 current = i == steps - 1 ? to : getRopePos(from, to, (i + 1) / (double) steps);
            current = current.subtract(basePos);
            mpos.set(current.x, current.y, current.z);
            int stepLight = level.getLightEngine().getRawBrightness(mpos, 0);
            stepLight = LightTexture.pack(stepLight, stepLight);
            this.connect(pose, vertex, stepLight, last, current, offsetNormals, r, g, b);
            last = current;
        }
        
        int items = (int) Math.max(3, Math.round(distance * ITEMS_PER_BLOCK));
        TagAccess tags = TagAccess.create(level);
        Quaternion alignRot = Vector3f.YP.rotation((float) Math.atan2(to.x - from.x, to.z - from.z));
        for (int i = 0; i < items; i++) {
            Vec3 pos = getRopePos(from, to, (i + 1) / (double) (items + 1));
            pos = pos.subtract(basePos);
            mpos.set(pos.x, pos.y, pos.z);
            int stepLight = level.getLightEngine().getRawBrightness(mpos, 0);
            stepLight = LightTexture.pack(stepLight, stepLight);
            this.renderItem(random, tags, poseStack, buffer, stepLight, pos, alignRot);
        }
    }

    private void connect(PoseStack.Pose pose, VertexConsumer vertex, int light, Vec3 last, Vec3 current, Vec3[] offsetNormals, float r, float g, float b) {
        for (int i = 0; i < 4; i++) {
            Vec3 off1 = offsetNormals[i];
            Vec3 off2 = offsetNormals[(i + 1) % 4];
            Vec3 normal = offsetNormals[i + 4];
            
            vertex.vertex(pose.pose(), (float) (last.x + off1.x), (float) (last.y + off1.y), (float) (last.z + off1.z)).color(r, g, b, 1f).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(pose.normal(), (float) normal.x, (float) normal.y, (float) normal.z).endVertex();
            vertex.vertex(pose.pose(), (float) (last.x + off2.x), (float) (last.y + off2.y), (float) (last.z + off2.z)).color(r, g, b, 1f).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(pose.normal(), (float) normal.x, (float) normal.y, (float) normal.z).endVertex();
            vertex.vertex(pose.pose(), (float) (current.x + off2.x), (float) (current.y + off2.y), (float) (current.z + off2.z)).color(r, g, b, 1f).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(pose.normal(), (float) normal.x, (float) normal.y, (float) normal.z).endVertex();
            vertex.vertex(pose.pose(), (float) (current.x + off1.x), (float) (current.y + off1.y), (float) (current.z + off1.z)).color(r, g, b, 1f).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(pose.normal(), (float) normal.x, (float) normal.y, (float) normal.z).endVertex();

            vertex.vertex(pose.pose(), (float) (current.x + off1.x), (float) (current.y + off1.y), (float) (current.z + off1.z)).color(r, g, b, 1f).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(pose.normal(), (float) normal.x, (float) normal.y, (float) normal.z).endVertex();
            vertex.vertex(pose.pose(), (float) (current.x + off2.x), (float) (current.y + off2.y), (float) (current.z + off2.z)).color(r, g, b, 1f).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(pose.normal(), (float) normal.x, (float) normal.y, (float) normal.z).endVertex();
            vertex.vertex(pose.pose(), (float) (last.x + off2.x), (float) (last.y + off2.y), (float) (last.z + off2.z)).color(r, g, b, 1f).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(pose.normal(), (float) normal.x, (float) normal.y, (float) normal.z).endVertex();
            vertex.vertex(pose.pose(), (float) (last.x + off1.x), (float) (last.y + off1.y), (float) (last.z + off1.z)).color(r, g, b, 1f).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(pose.normal(), (float) normal.x, (float) normal.y, (float) normal.z).endVertex();
        }
    }

    private void renderItem(Random random, TagAccess tags, PoseStack poseStack, MultiBufferSource buffer, int light, Vec3 off, Quaternion alignRot) {
        Item item;
        if (random.nextBoolean()) {
            item = ModItems.candy.random(random);
        } else {
            item = tags.random(ModItemTags.GARLAND_ITEMS, RandomSource.create(random.nextLong())).orElse(Items.BARRIER);
        }
        ItemStack stack = new ItemStack(item);
        poseStack.pushPose();
        poseStack.translate(off.x, off.y - GARLAND_RADIUS, off.z);
        poseStack.mulPose(alignRot);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        if (stack.is(ItemTags.FLOWERS)) {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(180));
        } else {
            poseStack.translate(0, -0.5, 0);
        }
        poseStack.scale(2f, 2f, 2f);
        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack, ItemTransforms.TransformType.GROUND, light, OverlayTexture.NO_OVERLAY,
                poseStack, buffer, 0
        );
        poseStack.popPose();
    }

    private static Vec3 getHangingPoint(BlockState state, BlockPos pos) {
        Direction dir = Direction.UP;
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        }
        double xOff = 0.5;
        double zOff = 0.5;
        if (dir.getAxis() == Direction.Axis.X) xOff = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 13/16d : 3/16d;
        if (dir.getAxis() == Direction.Axis.Z) zOff = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 13/16d : 3/16d;
        return new Vec3(pos.getX() + xOff, pos.getY() + 0.5, pos.getZ() + zOff);
    }
    
    private static Vec3 getRopePos(Vec3 from, Vec3 to, double pct) {
        double dstFactor = Mth.lerp(from.distanceTo(to) / GarlandBlock.MAX_DISTANCE, MIN_DIST_F, MAX_DIST_F);
        double maxValue = HEIGHT_FACTOR * Math.cosh(dstFactor);
        double value = (HEIGHT_FACTOR * Math.cosh(dstFactor * ((pct * 2) - 1))) - maxValue;
        double linearHeight = Mth.lerp(pct, from.y, to.y);
        return new Vec3(Mth.lerp(pct, from.x, to.x), linearHeight + value, Mth.lerp(pct, from.z, to.z));
    }
    
    private static Vec3[] getOffsetNormals(Vec3 from, Vec3 to) {
        Vec3 n1 = new Vec3(from.z - to.z, 0, to.x - from.x).normalize();
        return new Vec3[]{
            // offset vectors
            n1.scale(SQRT_HALF).add(VEC_UP.scale(SQRT_HALF)).scale(GARLAND_RADIUS),
            n1.scale(-SQRT_HALF).add(VEC_UP.scale(SQRT_HALF)).scale(GARLAND_RADIUS),
            n1.scale(-SQRT_HALF).add(VEC_UP.scale(-SQRT_HALF)).scale(GARLAND_RADIUS),
            n1.scale(SQRT_HALF).add(VEC_UP.scale(-SQRT_HALF)).scale(GARLAND_RADIUS),
            // normals
            VEC_UP,
            n1.scale(-1),
            VEC_UP.scale(-1),
            n1
        };
    }
}
