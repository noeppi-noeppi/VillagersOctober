package io.github.noeppi_noeppi.mods.villagersoctober.dress;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DressHelper {
    
    private static final List<Dress> dresses = new ArrayList<>();
    
    public static boolean hasDress(Player player) {
        for (Dress dress : dresses) {
            if (dress.hasDress(player)) return true;
        }
        return false;
    }
    
    public static void addDress(@Nullable Item head, @Nullable Item chest) {
        addDress(head, chest, null, null);
    }
    
    public static void addDress(@Nullable Item head, @Nullable Item chest, @Nullable Item legs, @Nullable Item feet) {
        if (head == null && chest == null && legs == null && feet == null) throw new IllegalArgumentException("Empty dress");
        dresses.add(new Dress(head, chest, legs, feet));
    }
    
    private record Dress(
            @Nullable Item head,
            @Nullable Item chest,
            @Nullable Item legs,
            @Nullable Item feet
    ) {
        
        @SuppressWarnings("RedundantIfStatement")
        public boolean hasDress(Player player) {
            ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
            if (this.head == null ? !head.isEmpty() : !head.is(this.head)) return false;
            if (this.chest == null ? !chest.isEmpty() : !chest.is(this.chest)) return false;
            if (this.legs == null ? !legs.isEmpty() : !legs.is(this.legs)) return false;
            if (this.feet == null ? !feet.isEmpty() : !feet.is(this.feet)) return false;
            return true;
        }
    }
}
