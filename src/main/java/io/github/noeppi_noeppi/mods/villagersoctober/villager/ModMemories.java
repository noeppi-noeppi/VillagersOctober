package io.github.noeppi_noeppi.mods.villagersoctober.villager;

import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import org.moddingx.libx.annotation.registration.RegisterClass;

import java.util.Optional;

@RegisterClass(registry = "MEMORY_MODULE_TYPE_REGISTRY")
public class ModMemories {
    
    public static final MemoryModuleType<Player> candyTarget = new MemoryModuleType<>(Optional.empty());
}
