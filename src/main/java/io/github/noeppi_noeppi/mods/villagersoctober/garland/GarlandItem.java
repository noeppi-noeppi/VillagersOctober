package io.github.noeppi_noeppi.mods.villagersoctober.garland;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GarlandItem extends BlockItem {

    public GarlandItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            ItemStack stack = player.getItemInHand(hand);
            CompoundTag nbt = stack.getTag();
            if (nbt != null) {
                nbt.remove("StoredPos");
                nbt.remove("StoredDir");
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        } else {
            return super.use(level, player, hand);
        }
    }

    @Override
    protected boolean placeBlock(@Nonnull BlockPlaceContext context, @Nonnull BlockState state) {
        ItemStack stack = context.getItemInHand();
        CompoundTag nbt = stack.getOrCreateTag();
        if (nbt.contains("StoredPos") && nbt.contains("StoredDir")) {
            BlockPos otherPos = NbtUtils.readBlockPos(nbt.getCompound("StoredPos")).immutable();
            Direction dir = Direction.from2DDataValue(nbt.getInt("StoredDir"));
            BlockState otherState = this.getBlock().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, dir);
            if (!context.getLevel().isEmptyBlock(otherPos) || !otherState.canSurvive(context.getLevel(), otherPos)) {
                nbt.remove("StoredPos");
                nbt.remove("StoredDir");
                if (context.getPlayer() != null) {
                    context.getPlayer().displayClientMessage(Component.translatable("tooltip.villagersoctober.garland.origin_obstructed"), true);
                }
                return false;
            }
            if (otherPos.atY(0).distSqr(context.getClickedPos().atY(0)) < 3 * 3) {
                if (context.getPlayer() != null) {
                    context.getPlayer().displayClientMessage(Component.translatable("tooltip.villagersoctober.garland.too_close"), true);
                }
                return false;
            }
            if (otherPos.distSqr(context.getClickedPos()) > GarlandBlock.MAX_DISTANCE * GarlandBlock.MAX_DISTANCE) {
                if (context.getPlayer() != null) {
                    context.getPlayer().displayClientMessage(Component.translatable("tooltip.villagersoctober.garland.too_far"), true);
                }
                return false;
            }
            if (Math.abs(otherPos.getY() - context.getClickedPos().getY()) > 4) {
                if (context.getPlayer() != null) {
                    context.getPlayer().displayClientMessage(Component.translatable("tooltip.villagersoctober.garland.too_far"), true);
                }
                return false;
            }
            if (super.placeBlock(context, state)) {
                context.getLevel().setBlock(otherPos, otherState, 3);
                if (context.getLevel().getBlockEntity(otherPos) instanceof Garland garland) {
                    garland.setPrimary(false);
                    garland.setOtherPos(context.getClickedPos());
                }
                if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof Garland garland) {
                    garland.setPrimary(true);
                    garland.setOtherPos(otherPos);
                }
                nbt.remove("StoredPos");
                nbt.remove("StoredDir");
                return true;
            } else {
                return false;
            }
        } else {
            nbt.put("StoredPos", NbtUtils.writeBlockPos(context.getClickedPos()));
            nbt.putInt("StoredDir", state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue());
            if (context.getPlayer() != null) {
                context.getPlayer().displayClientMessage(Component.translatable("tooltip.villagersoctober.garland.connect", posString(context.getClickedPos())), true);
            }
            return false;
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.contains("StoredPos") && nbt.contains("StoredDir")) {
            tooltip.add(Component.translatable("tooltip.villagersoctober.garland.connect", posString(NbtUtils.readBlockPos(nbt.getCompound("StoredPos")))).withStyle(ChatFormatting.GOLD));
        }
    }

    private static String posString(BlockPos pos) {
        return "[" + pos.getY() + "," + pos.getY() + "," + pos.getZ() + "]";
    }
}
