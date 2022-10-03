package io.github.noeppi_noeppi.mods.villagersoctober.dress;

import io.github.noeppi_noeppi.mods.villagersoctober.dress.render.DressRenderProperties;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.moddingx.libx.base.ItemBase;
import org.moddingx.libx.mod.ModX;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DressItem extends ItemBase {

    public final EquipmentSlot slot;
    
    public DressItem(ModX mod, EquipmentSlot slot, Properties properties) {
        super(mod, properties);
        this.slot = slot;
    }

    public boolean usesBlockModel() {
        return false;
    }
    
    @OnlyIn(Dist.CLIENT)
    public DressRenderProperties getRenderProperties() {
        return DressRenderProperties.EMPTY;
    }

    @Nullable
    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return this.slot;
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return armorType == this.slot;
    }
    
    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack current = player.getItemBySlot(this.slot);
        if (current.isEmpty()) {
            player.setItemSlot(slot, stack.copy());
            if (!level.isClientSide) player.awardStat(Stats.ITEM_USED.get(this));
            stack.setCount(0);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }
}
