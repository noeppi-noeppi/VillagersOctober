package io.github.noeppi_noeppi.mods.villagersoctober.garland;

import io.github.noeppi_noeppi.mods.villagersoctober.ModBlocks;
import io.github.noeppi_noeppi.mods.villagersoctober.util.BlockClipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import org.moddingx.libx.base.tile.BlockEntityBase;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Garland extends BlockEntityBase {
    
    private static final Direction[] HORIZONTAL_DIRS = new Direction[]{ Direction.SOUTH, Direction.EAST, Direction.NORTH, Direction.WEST };
    
    private boolean primary;
    private BlockPos otherPos;
    private AABB renderAABB;
    private boolean generating;
    
    public Garland(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.primary = false;
        this.otherPos = BlockPos.ZERO;
        this.generating = false;
        this.updateRenderAABB();
    }
    
    public boolean primary() {
        return primary;
    }

    public BlockPos otherPos() {
        return otherPos;
    }

    public boolean generateOnLoad() {
        return generating;
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
    
    public void setGenerateOnLoad() {
        this.primary = false;
        this.otherPos = BlockPos.ZERO;
        this.updateRenderAABB();
        this.generating = true;
        this.setChanged();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.generating) {
            boolean generated = false;
            if (this.level != null && !this.level.isClientSide && this.getBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Map<BlockPos, BlockState> possiblePositions = new HashMap<>();
                int minX = this.worldPosition.getX() - 14;
                int maxX = this.worldPosition.getX() + 14;
                int minY = this.worldPosition.getY() - 2;
                int maxY = this.worldPosition.getY() + 2;
                int minZ = this.worldPosition.getZ() - 14;
                int maxZ = this.worldPosition.getZ() + 14;
                Direction currentDir = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
                switch (currentDir) {
                    case NORTH -> minZ = this.worldPosition.getZ() + 4;
                    case SOUTH -> maxZ = this.worldPosition.getZ() - 4;
                    case WEST -> minX = this.worldPosition.getX() + 4;
                    case EAST -> maxX = this.worldPosition.getX() - 4;
                }
                BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
                for (int x = minX; x <= maxX; x++) {
                    for (int y = minY; y <= maxY; y++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            mpos.set(x, y, z);
                            if (this.level.isEmptyBlock(mpos)) {
                                for (Direction dir : HORIZONTAL_DIRS) {
                                    if (dir == currentDir) continue;
                                    BlockState state = ModBlocks.garland.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, dir);
                                    if (state.canSurvive(this.level, mpos) && BlockClipHelper.isFreeSpace(this.level, this.worldPosition, mpos)) {
                                        possiblePositions.put(mpos.immutable(), state);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (!possiblePositions.isEmpty()) {
                    List<BlockPos> positions = possiblePositions.keySet().stream().toList();
                    BlockPos pos = positions.get(new Random(Mth.getSeed(this.worldPosition) + 42).nextInt(positions.size()));
                    if (this.level.setBlock(pos, possiblePositions.get(pos), 3)) {
                        if (this.level.getBlockEntity(pos) instanceof Garland garland) {
                            generated = true;
                            garland.setPrimary(false);
                            garland.setOtherPos(this.worldPosition);
                            this.setPrimary(true);
                            this.setOtherPos(pos);
                        }
                    }
                }
            }
            this.generating = false;
            this.setChanged();
            if (generated) {
                this.setDispatchable();
            } else if (this.level != null) {
                this.level.destroyBlock(this.worldPosition, false);
            }
        }
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.primary = nbt.getBoolean("Primary");
        this.otherPos = NbtUtils.readBlockPos(nbt.getCompound("OtherPos")).immutable();
        this.generating = nbt.getBoolean("GenerateOnLoad");
        this.updateRenderAABB();
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean("Primary", this.primary);
        nbt.put("OtherPos", NbtUtils.writeBlockPos(this.otherPos));
        nbt.putBoolean("GenerateOnLoad", this.generating);
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
