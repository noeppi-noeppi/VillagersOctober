package io.github.noeppi_noeppi.mods.villagersoctober.core;

import io.github.noeppi_noeppi.mods.villagersoctober.dress.DressItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
public class CoreModUtil {
    
    public static boolean shouldRenderHead(Entity entity) {
        if (!(entity instanceof LivingEntity living)) return true;
        ItemStack stack = living.getItemBySlot(EquipmentSlot.HEAD);
        return !(stack.getItem() instanceof DressItem dress) || dress.usesBlockModel();
    }
}
