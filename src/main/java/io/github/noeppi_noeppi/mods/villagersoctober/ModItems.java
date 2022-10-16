package io.github.noeppi_noeppi.mods.villagersoctober;

import io.github.noeppi_noeppi.mods.villagersoctober.bloon.GhastBalloonItem;
import io.github.noeppi_noeppi.mods.villagersoctober.content.CandyItem;
import io.github.noeppi_noeppi.mods.villagersoctober.dress.*;
import io.github.noeppi_noeppi.mods.villagersoctober.util.Colored;
import net.minecraft.world.item.Item;
import org.moddingx.libx.annotation.registration.Reg;
import org.moddingx.libx.annotation.registration.RegisterClass;

@RegisterClass(registry = "ITEM_REGISTRY")
public class ModItems {
    
    @Reg.Multi public static final Colored<CandyItem> candy = new Colored<>(color -> new CandyItem(VillagersOctober.getInstance(), color));
    public static final GhastBalloonItem ghastBalloon = new GhastBalloonItem(VillagersOctober.getInstance(), new Item.Properties().stacksTo(1));
    public static final WitchHat witchHat = new WitchHat(VillagersOctober.getInstance(), new Item.Properties().stacksTo(1));
    public static final WitchRobe witchRobe = new WitchRobe(VillagersOctober.getInstance(), new Item.Properties().stacksTo(1));
    public static final BatMask batMask = new BatMask(VillagersOctober.getInstance(), new Item.Properties().stacksTo(1));
    public static final BatWings batWings = new BatWings(VillagersOctober.getInstance(), new Item.Properties().stacksTo(1));
    public static final VampireCloak vampireCloak = new VampireCloak(VillagersOctober.getInstance(), new Item.Properties().stacksTo(1));
}
