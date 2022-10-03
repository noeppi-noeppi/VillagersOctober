package io.github.noeppi_noeppi.mods.villagersoctober.dress;

import net.minecraft.world.entity.EquipmentSlot;
import org.moddingx.libx.mod.ModX;

public class WitchHat extends DressItem {

    public WitchHat(ModX mod, Properties properties) {
        super(mod, EquipmentSlot.HEAD, properties);
    }

    @Override
    public boolean usesBlockModel() {
        return true;
    }
}
