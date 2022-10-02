package io.github.noeppi_noeppi.mods.villagersoctober.scarecrow;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.data.ModelData;
import org.moddingx.libx.annotation.model.Model;

import javax.annotation.Nonnull;
import java.util.Random;

public class ScarecrowRenderer implements BlockEntityRenderer<Scarecrow> {

    @Model("block/scarecrow_0") public static BakedModel model0;
    @Model("block/scarecrow_1") public static BakedModel model1;
    @Model("block/scarecrow_2") public static BakedModel model2;
    @Model("block/scarecrow_3") public static BakedModel model3;
    @Model("block/scarecrow_4") public static BakedModel model4;
    @Model("block/scarecrow_5") public static BakedModel model5;

    @Override
    public void render(@Nonnull Scarecrow scarecrow, float partialTick, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        BakedModel model = this.getModel(scarecrow.getBlockPos());
        float anim = Math.max(0, scarecrow.getAnimationTime() - partialTick);
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        if (anim > 0) {
            double x80 = anim / 80;
            float amount = (float) (x80 * x80 * x80 * -Math.sin(anim * Math.PI / 5));
            poseStack.mulPose(Vector3f.YP.rotation((float) scarecrow.getAnimationAngle()));
            poseStack.mulPose(Vector3f.XP.rotationDegrees(10 * amount));
            poseStack.mulPose(Vector3f.YP.rotation((float) -scarecrow.getAnimationAngle()));
        }
        int rot = scarecrow.getBlockState().getValue(BlockStateProperties.ROTATION_16);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-(rot * 360) / 16f));
        poseStack.translate(-0.5, 0, -0.5);

        VertexConsumer vertex = buffer.getBuffer(RenderType.solid());
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                poseStack.last(), vertex, scarecrow.getBlockState(), model,
                1, 1, 1, light, overlay, ModelData.EMPTY,
                RenderType.solid()
        );
        
        poseStack.popPose();
    }

    private BakedModel getModel(BlockPos pos) {
        return switch (new Random(Mth.getSeed(pos)).nextInt(6)) {
            case 1 -> model1;
            case 2 -> model2;
            case 3 -> model3;
            case 4 -> model4;
            case 5 -> model5;
            default -> model0;
        };
    }
}
