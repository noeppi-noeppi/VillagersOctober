package io.github.noeppi_noeppi.mods.villagersoctober.advancement;

import net.minecraft.advancements.CriteriaTriggers;

public class ModTriggers {
    
    public static final HalloweenTrigger HALLOWEEN = new HalloweenTrigger();

    public static void setup() {
        CriteriaTriggers.register(HALLOWEEN);
    }
}
