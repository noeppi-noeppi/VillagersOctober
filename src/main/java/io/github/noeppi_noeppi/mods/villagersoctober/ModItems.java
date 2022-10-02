package io.github.noeppi_noeppi.mods.villagersoctober;

import io.github.noeppi_noeppi.mods.villagersoctober.content.CandyItem;
import io.github.noeppi_noeppi.mods.villagersoctober.util.Colored;
import org.moddingx.libx.annotation.registration.Reg;
import org.moddingx.libx.annotation.registration.RegisterClass;

@RegisterClass(registry = "ITEM_REGISTRY")
public class ModItems {
    
    @Reg.Multi public static final Colored<CandyItem> candy = new Colored<>(color -> new CandyItem(VillagersOctober.getInstance(), color));
}
