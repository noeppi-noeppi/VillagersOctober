package io.github.noeppi_noeppi.mods.villagersoctober.util;

import net.minecraft.world.item.DyeColor;
import org.moddingx.libx.registration.util.EnumObjects;

import java.util.function.Function;

public class Colored<T> extends EnumObjects<DyeColor, T> {
    
    public Colored(Function<DyeColor, T> factory) {
        super(DyeColor.class, factory);
    }
}
