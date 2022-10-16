package io.github.noeppi_noeppi.mods.villagersoctober.villager.profession;

import io.github.noeppi_noeppi.mods.villagersoctober.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class MadManTrades {
    
    public static Int2ObjectMap<VillagerTrades.ItemListing[]> makeTrades() {
        Map<Integer, VillagerTrades.ItemListing[]> map = new HashMap<>();
        map.put(1, new VillagerTrades.ItemListing[]{
                new VillagerTrades.EmeraldForItems(Items.BONE, 8, 16, 3),
                new VillagerTrades.ItemsForEmeralds(Items.GLOW_BERRIES, 7, 2, 2)
        });
        map.put(2, new VillagerTrades.ItemListing[]{
                new VillagerTrades.EmeraldForItems(Items.FERMENTED_SPIDER_EYE, 2, 12, 10),
                new VillagerTrades.ItemsForEmeralds(Items.JACK_O_LANTERN, 5, 1, 8)
        });
        map.put(3, new VillagerTrades.ItemListing[]{
                new VillagerTrades.EmeraldForItems(Blocks.SOUL_SAND, 16, 12, 14),
                new VillagerTrades.EmeraldForItems(Blocks.SOUL_LANTERN, 2, 1, 20),
                new VillagerTrades.ItemsForEmeralds(Items.COAL, 3, 7, 15)
        });
        map.put(4, new VillagerTrades.ItemListing[]{
                new VillagerTrades.ItemsForEmeralds(Items.PUMPKIN_PIE, 2, 4, 20)
        });
        map.put(5, new VillagerTrades.ItemListing[]{
                new VillagerTrades.ItemsForEmeralds(ModItems.ghastBalloon, 42, 1, 1, 30),
        });
        return new Int2ObjectOpenHashMap<>(Map.copyOf(map));
    }
}
