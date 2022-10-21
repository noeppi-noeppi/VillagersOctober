package io.github.noeppi_noeppi.mods.villagersoctober.bell;

import io.github.noeppi_noeppi.mods.villagersoctober.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.moddingx.libx.base.BlockBase;
import org.moddingx.libx.block.RotationShape;
import org.moddingx.libx.mod.ModX;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DoorBellBlock extends BlockBase {

    public static final RotationShape SHAPE = new RotationShape(Shapes.or(
            box(6, 5, 0, 10, 11, 1),
            box(7, 7, 1, 9, 9, 2)
    ));

    public static final RotationShape SHAPE_PRESSED = new RotationShape(Shapes.or(
            box(6, 5, 0, 10, 11, 1),
            box(7, 7, 0.25, 9, 9, 1.25)
    ));

    public DoorBellBlock(ModX mod, Properties properties) {
        super(mod, properties, new Item.Properties().stacksTo(16));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(BlockStateProperties.POWERED, false)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.POWERED);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection());
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return (state.getValue(BlockStateProperties.POWERED) ? SHAPE_PRESSED : SHAPE).getShape(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos) {
        Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        return canSupportCenter(level, pos.relative(dir), dir.getOpposite());
    }
    
    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        return facing == dir && !this.canSurvive(state, level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        if (state.getValue(BlockStateProperties.POWERED)) {
            return InteractionResult.CONSUME;
        } else {
            level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, true), 3);
            level.scheduleTick(pos, this, 30);
            level.playSound(null, pos, ModSounds.doorbell, SoundSource.BLOCKS, 0.3f, 0.5f);
            level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
            BellHelper.wakeUpVillager(level, pos, player);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, false), 3);
        level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
    }
}
