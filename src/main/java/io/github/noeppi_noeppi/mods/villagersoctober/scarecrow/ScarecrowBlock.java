package io.github.noeppi_noeppi.mods.villagersoctober.scarecrow;

import io.github.noeppi_noeppi.mods.villagersoctober.ModBlocks;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.moddingx.libx.base.tile.BlockBE;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.SetupContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ScarecrowBlock extends BlockBE<Scarecrow> {

    public static final VoxelShape SHAPE = Shapes.or(
            box(7.5, 0, 7.5, 8.5, 7, 8.5),
            box(4, 7, 4, 12, 16, 12),
            box(3, 16, 3, 13, 26, 13)
    );
    
    public ScarecrowBlock(ModX mod, Properties properties) {
        super(mod, Scarecrow.class, properties); // Block entity only required for renderer
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(BlockStateProperties.ROTATION_16, 0)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(SetupContext ctx) {
        ctx.enqueue(() -> BlockEntityRenderers.register(this.getBlockEntityType(), c -> new ScarecrowRenderer()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.ROTATION_16);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        if (!ctx.getLevel().getBlockState(ctx.getClickedPos().above()).canBeReplaced(ctx)) return null;
        return this.defaultBlockState().setValue(BlockStateProperties.ROTATION_16, Mth.floor((double) (ctx.getRotation() * 16 / 360f) + 0.5) & 0x0F);
    }

    @Override
    public void setPlacedBy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
        if (!level.isClientSide) {
            level.setBlock(pos.above(), ModBlocks.scarecrowTop.defaultBlockState(), 3);
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public float getDestroyProgress(@Nonnull BlockState state, @Nonnull Player player, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return 1;
    }
    
    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (level.getBlockEntity(pos) instanceof Scarecrow scarecrow && !scarecrow.hit(player)) {
            // Set the block back and notify the client
            level.setBlock(pos, state, 3);
            if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundBlockUpdatePacket(level, pos));
            }
            level.playSound(player, pos, SoundEvents.CROSSBOW_SHOOT, SoundSource.BLOCKS, 1, 1);
            return false;
        } else {
            return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
        }
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        if (facing == Direction.UP) {
            return facingState.getBlock() == ModBlocks.scarecrowTop ? state : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        }
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);
        if (level.getBlockState(pos.above()).getBlock() == ModBlocks.scarecrowTop) {
            level.destroyBlock(pos.above(), false);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onProjectileHit(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockHitResult hit, @Nonnull Projectile projectile) {
        if (level.getBlockEntity(hit.getBlockPos()) instanceof Scarecrow scarecrow) {
            scarecrow.hitFrom(hit.getLocation().subtract(projectile.getDeltaMovement().normalize()));
        }
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        return PushReaction.DESTROY;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
