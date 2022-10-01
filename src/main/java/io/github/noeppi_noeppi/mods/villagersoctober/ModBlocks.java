package io.github.noeppi_noeppi.mods.villagersoctober;

import io.github.noeppi_noeppi.mods.villagersoctober.village.DoorBell;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.moddingx.libx.annotation.registration.RegisterClass;

@RegisterClass(registry = "BLOCK_REGISTRY")
public class ModBlocks {
    
    public static final DoorBell doorbell = new DoorBell(VillagersOctober.getInstance(), BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON));
}
