package io.github.noeppi_noeppi.mods.villagersoctober;

import io.github.noeppi_noeppi.mods.villagersoctober.scarecrow.ScarecrowBlockTop;
import io.github.noeppi_noeppi.mods.villagersoctober.bell.DoorBellBlock;
import io.github.noeppi_noeppi.mods.villagersoctober.scarecrow.ScarecrowBlock;
import io.github.noeppi_noeppi.mods.villagersoctober.table.MysticalTableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.moddingx.libx.annotation.registration.RegisterClass;

@RegisterClass(registry = "BLOCK_REGISTRY")
public class ModBlocks {
    
    public static final DoorBellBlock doorbell = new DoorBellBlock(VillagersOctober.getInstance(), BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON));
    public static final ScarecrowBlock scarecrow = new ScarecrowBlock(VillagersOctober.getInstance(), BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK));
    public static final Block scarecrowTop = new ScarecrowBlockTop(VillagersOctober.getInstance(), BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK));
    public static final MysticalTableBlock mysticalTable = new MysticalTableBlock(VillagersOctober.getInstance(), BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS));
}
