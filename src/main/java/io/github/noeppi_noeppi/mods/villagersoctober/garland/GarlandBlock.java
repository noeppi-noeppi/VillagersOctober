package io.github.noeppi_noeppi.mods.villagersoctober.garland;

import io.github.noeppi_noeppi.mods.villagersoctober.VillagersOctober;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.moddingx.libx.base.tile.BlockBE;
import org.moddingx.libx.block.RotationShape;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class GarlandBlock extends BlockBE<Garland> {

    public static final int MAX_DISTANCE = 20;

    private static final RotationShape SHAPE = new RotationShape(box(5.5, 5.5, 0, 10.5, 10.5, 3));
    
    private final GarlandItem item;
    
    public GarlandBlock(ModX mod, Properties properties) {
        super(mod, Garland.class, properties, null);
        this.item = new GarlandItem(this, new Item.Properties().tab(Objects.requireNonNull(VillagersOctober.getInstance().tab)));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
        );
    }

    @Override
    public void registerAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerAdditional(ctx, builder);
        builder.register(Registry.ITEM_REGISTRY, this.item);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(SetupContext ctx) {
        ctx.enqueue(() -> BlockEntityRenderers.register(this.getBlockEntityType(), c -> new GarlandRenderer()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
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
        return SHAPE.getShape(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }
    
    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos) {
        Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        return canSupportCenter(level, pos.relative(dir), dir.getOpposite());
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (!level.isClientSide && !state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof Garland garland) {
            if (level.getBlockState(garland.otherPos()).getBlock() == this) {
                // Don't drop, as this block will drop, so in total only one garland drops
                level.destroyBlock(garland.otherPos(), false);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
