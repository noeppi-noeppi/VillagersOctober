package io.github.noeppi_noeppi.mods.villagersoctober.villager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import io.github.noeppi_noeppi.mods.villagersoctober.bell.GiveCandyBehavior;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.stream.Stream;

public class VillagerBrainHelper {

    public static Brain.Provider<Villager> wrapBrain(Brain.Provider<Villager> provider) {
        return Brain.provider(Streams.concat(provider.memoryTypes.stream(), Stream.of(
                ModMemories.candyTarget
        )).distinct().toList(), provider.sensorTypes);
    }
    
    public static void initialiseBrain(Villager villager, Brain<Villager> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(ModActivities.giveCandy, 0, ImmutableList.of(new GiveCandyBehavior()), ModMemories.candyTarget);
    }
    
    public static void navigate(Villager villager, Vec3 target, double speed) {
        Path path = villager.getNavigation().createPath(target.x, target.y, target.z, 1);
        villager.getNavigation().moveTo(path, speed);
        villager.getBrain().setMemory(MemoryModuleType.PATH, path);
    }
    
    public static void stay(Villager villager) {
        villager.getNavigation().stop();
        villager.getBrain().eraseMemory(MemoryModuleType.PATH);
    }
}
