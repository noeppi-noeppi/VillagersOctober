package io.github.noeppi_noeppi.mods.villagersoctober.table;

import com.google.common.collect.Range;
import io.github.noeppi_noeppi.mods.villagersoctober.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.moddingx.libx.base.tile.BlockEntityBase;
import org.moddingx.libx.capability.ItemCapabilities;
import org.moddingx.libx.inventory.BaseItemStackHandler;
import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MysticalTable extends BlockEntityBase {

    private final BaseItemStackHandler inventory = BaseItemStackHandler.builder(5)
            .contentsChanged(() -> {
                this.setChanged();
                this.setDispatchable();
            })
            .defaultSlotLimit(1)
            .validator(MysticalTable::validStack, Range.all())
            .build();
    
    private final LazyOptional<IAdvancedItemHandlerModifiable> capability = ItemCapabilities.create(this::getInventory);
    
    public MysticalTable(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.capability.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("Inventory", this.inventory.serializeNBT());
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        if (this.level != null && !this.level.isClientSide) {
            nbt.put("Inventory", this.inventory.serializeNBT());
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        super.handleUpdateTag(nbt);
        if (this.level != null && this.level.isClientSide) {
            this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
        }
    }

    public static boolean validStack(ItemStack stack) {
        return BrewingRecipeRegistry.isValidIngredient(stack) || stack.is(ModItemTags.MYSTICAL_TABLE_ITEMS);
    }
}
