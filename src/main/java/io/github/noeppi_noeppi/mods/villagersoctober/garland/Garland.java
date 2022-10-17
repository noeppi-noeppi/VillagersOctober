package io.github.noeppi_noeppi.mods.villagersoctober.garland;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.moddingx.libx.base.tile.BlockEntityBase;

import javax.annotation.Nonnull;

public class Garland extends BlockEntityBase {
    
    private boolean primary;
    private BlockPos otherPos;
    private AABB renderAABB;
    
    public Garland(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.primary = false;
        this.otherPos = BlockPos.ZERO;
        this.updateRenderAABB();
    }
    
    public boolean primary() {
        return primary;
    }

    public BlockPos otherPos() {
        return otherPos;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
        this.setChanged();
        this.setDispatchable();
    }

    public void setOtherPos(BlockPos otherPos) {
        this.otherPos = otherPos.immutable();
        this.setChanged();
        this.setDispatchable();
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.primary = nbt.getBoolean("Primary");
        this.otherPos = NbtUtils.readBlockPos(nbt.getCompound("OtherPos")).immutable();
        this.updateRenderAABB();
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean("Primary", this.primary);
        nbt.put("OtherPos", NbtUtils.writeBlockPos(this.otherPos));
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        if (this.level != null && !this.level.isClientSide) {
            nbt.putBoolean("Primary", this.primary);
            nbt.put("OtherPos", NbtUtils.writeBlockPos(this.otherPos));
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        super.handleUpdateTag(nbt);
        if (this.level != null && this.level.isClientSide) {
            this.primary = nbt.getBoolean("Primary");
            this.otherPos = NbtUtils.readBlockPos(nbt.getCompound("OtherPos")).immutable();
            this.updateRenderAABB();
        }
    }

    private void updateRenderAABB() {
        int minX = Math.min(this.worldPosition.getX(), this.otherPos.getX());
        int maxX = Math.max(this.worldPosition.getX() + 1, this.otherPos.getX() + 1);
        
        int minY = Math.min(this.worldPosition.getY() - 4, this.otherPos.getY() - 4);
        int maxY = Math.max(this.worldPosition.getY() + 1, this.otherPos.getY() + 1);

        int minZ = Math.min(this.worldPosition.getZ(), this.otherPos.getZ());
        int maxZ = Math.max(this.worldPosition.getZ() + 1, this.otherPos.getZ() + 1);
        
        this.renderAABB = new AABB(minX, minY, minZ, maxX, maxY, maxZ).inflate(1);
    }
    
    @Override
    public AABB getRenderBoundingBox() {
        return this.renderAABB;
    }
}
