package io.github.noeppi_noeppi.mods.villagersoctober.bell;

import io.github.noeppi_noeppi.mods.villagersoctober.dress.DressHelper;
import io.github.noeppi_noeppi.mods.villagersoctober.villager.ModActivities;
import io.github.noeppi_noeppi.mods.villagersoctober.villager.ModMemories;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.Comparator;

public class BellHelper {
    
    public static void wakeUpVillager(Level level, BlockPos pos, Player player) {
        if (level.isClientSide || !level.isNight()) return;
        if (!DressHelper.hasDress(player)) return;
        Vec3 vec = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        Villager villager = level.getEntitiesOfClass(Villager.class, new AABB(vec, vec).inflate(10)).stream()
                .filter(v -> !v.isBaby())
                .filter(BellHelper::canGiveCandy)
                .min(Comparator.comparingDouble(v -> vec.distanceToSqr(v.position())))
                .orElse(null);
        if (villager == null) return;
        villager.getBrain().setMemory(ModMemories.candyTarget, player);
        villager.getBrain().setActiveActivityIfPossible(ModActivities.giveCandy);
    }
    
    public static void tickEntity(LivingEvent.LivingTickEvent event) {
        if (!event.getEntity().level.isClientSide && event.getEntity() instanceof Villager villager) {
            CompoundTag nbt = villager.getPersistentData();
            if (nbt.contains("VillagersOctoberCandyTimer")) {
                int candyTimer = nbt.getInt("VillagersOctoberCandyTimer");
                if (candyTimer <= 1) {
                    nbt.remove("VillagersOctoberCandyTimer");
                } else {
                    nbt.putInt("VillagersOctoberCandyTimer", candyTimer - 1);
                }
            }
        }
    }
    
    public static void setCandyTimer(Villager villager) {
        CompoundTag nbt = villager.getPersistentData();
        nbt.putInt("VillagersOctoberCandyTimer", 900 + villager.getRandom().nextInt(900));
    }
    
    private static boolean canGiveCandy(Villager villager) {
        CompoundTag nbt = villager.getPersistentData();
        return !nbt.contains("VillagersOctoberCandyTimer") || nbt.getInt("VillagersOctoberCandyTimer") <= 0;
    }
}
