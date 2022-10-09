package io.github.noeppi_noeppi.mods.villagersoctober.villager;

import io.github.noeppi_noeppi.mods.villagersoctober.VillagersOctober;
import net.minecraft.world.entity.schedule.Activity;
import org.moddingx.libx.annotation.registration.RegisterClass;

@RegisterClass(registry = "ACTIVITY_REGISTRY")
public class ModActivities {
    
    public static final Activity giveCandy = new Activity(VillagersOctober.getInstance().resource("give_candy").toString());
}
