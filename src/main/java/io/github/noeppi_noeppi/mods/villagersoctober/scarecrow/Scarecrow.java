package io.github.noeppi_noeppi.mods.villagersoctober.scarecrow;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.moddingx.libx.base.tile.BlockEntityBase;
import org.moddingx.libx.base.tile.TickingBlock;

import javax.annotation.Nonnull;

public class Scarecrow extends BlockEntityBase implements TickingBlock {

    private int hitCount;
    private int animationTime;
    private double animationAngle;
    
    public Scarecrow(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void tick() {
        if (this.animationTime > 0) {
            this.animationTime -= 1;
            if (this.animationTime == 0) {
                this.hitCount = 0;
                this.animationAngle = 0;
            }
            this.setChanged();
        }
    }
    
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hit(Player player) {
        if (this.hitCount++ >= 5) {
            this.setChanged();
            return true;
        } else {
            this.hitFrom(player.position());
            return false;
        }
    }
    
    public void hitFrom(Vec3 vec) {
        this.animationTime = 80;
        double xDiff = this.worldPosition.getX() + 0.5 - vec.x;
        double zDiff = this.worldPosition.getZ() + 0.5 - vec.z;
        this.animationAngle = Math.atan2(xDiff, zDiff);
        this.setChanged();
        this.setDispatchable();
    }

    public int getAnimationTime() {
        return animationTime;
    }

    public double getAnimationAngle() {
        return animationAngle;
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.hitCount = nbt.getInt("HitCount");
        this.animationTime = nbt.getInt("AnimationTime");
        this.animationAngle = nbt.getDouble("AnimationAngle");
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("HitCount", this.hitCount);
        nbt.putInt("AnimationTime", this.animationTime);
        nbt.putDouble("AnimationAngle", this.animationAngle);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        if (this.level != null && !this.level.isClientSide) {
            nbt.putInt("AnimationTime", this.animationTime);
            nbt.putDouble("AnimationAngle", this.animationAngle);
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        super.handleUpdateTag(nbt);
        if (this.level != null && this.level.isClientSide) {
            this.animationTime = nbt.getInt("AnimationTime");
            this.animationAngle = nbt.getDouble("AnimationAngle");
        }
    }
}
