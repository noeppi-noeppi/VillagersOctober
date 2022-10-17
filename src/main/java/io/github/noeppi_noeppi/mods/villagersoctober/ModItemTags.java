package io.github.noeppi_noeppi.mods.villagersoctober;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {

    public static final TagKey<Item> MYSTICAL_TABLE_ITEMS = ItemTags.create(VillagersOctober.getInstance().resource("mythical_table_items"));
    public static final TagKey<Item> GARLAND_ITEMS = ItemTags.create(VillagersOctober.getInstance().resource("garland_items"));
}
