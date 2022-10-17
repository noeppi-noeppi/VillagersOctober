package io.github.noeppi_noeppi.mods.villagersoctober.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BlockClipHelper {
    
    public static boolean isFreeSpace(Level level, BlockPos from, BlockPos to) {
        ClipContext ctx = new ClipContext(
                new Vec3(from.getX() + 0.5, from.getY() + 0.5, from.getZ() + 0.5),
                new Vec3(to.getX() + 0.5, to.getY() + 0.5, to.getZ() + 0.5),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null
        );
        return level.clip(ctx).getType() == HitResult.Type.MISS;
    }
}
