package io.github.noeppi_noeppi.mods.villagersoctober.bell;

import io.github.noeppi_noeppi.mods.villagersoctober.ModItems;
import io.github.noeppi_noeppi.mods.villagersoctober.advancement.ModTriggers;
import io.github.noeppi_noeppi.mods.villagersoctober.villager.ModMemories;
import io.github.noeppi_noeppi.mods.villagersoctober.villager.VillagerBrainHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Random;

public class GiveCandyBehavior extends Behavior<Villager> {
    
    private Player player;
    private Vec3 target;
    private int candyTimer;
    private ItemStack candyStack;
    
    public GiveCandyBehavior() {
        super(Map.of(
                ModMemories.candyTarget, MemoryStatus.VALUE_PRESENT
        ), 240);
    }

    @Override
    protected void start(@Nonnull ServerLevel level, @Nonnull Villager entity, long gameTime) {
        Player player = entity.getBrain().getMemory(ModMemories.candyTarget).orElse(null);
        if (player == null) {
            this.player = null;
            this.target = null;
            this.candyTimer = 0;
        } else {
            this.player = player;
            this.target = player.position();
            this.candyTimer = 0;
            if (entity.isSleeping()) entity.stopSleeping();
            VillagerBrainHelper.navigate(entity, this.target, 0.5);
        }
    }

    @Override
    protected void tick(@Nonnull ServerLevel level, @Nonnull Villager owner, long gameTime) {
        if (this.player == null || this.target == null) return;
        if (this.candyTimer <= 0) {
            if (this.target.distanceToSqr(owner.position()) <= 1.5 * 1.5) {
                VillagerBrainHelper.stay(owner);
                this.candyTimer = 1;
            } else if (owner.getNavigation().isStuck() || !owner.getNavigation().isInProgress()) {
                this.target = player.position();
                VillagerBrainHelper.navigate(owner, this.target, 0.5);
            }
        } else if (this.candyTimer > 60) {
            this.player = null;
            this.target = null;
            this.candyTimer = 0;
            this.candyStack = null;
            return;
        } else {
            this.candyTimer += 1;
        }
        
        if (candyTimer > 0) {
            VillagerBrainHelper.stay(owner);
            if (this.candyStack == null) {
                Random random = new Random();
                this.candyStack = new ItemStack(ModItems.candy.random(random), 1 + random.nextInt(2));
            }
            owner.lookAt(this.player, 20, 20);
            if (this.candyTimer < 40) {
                owner.setItemInHand(InteractionHand.MAIN_HAND, this.candyStack.copy());
            } else {
                owner.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            }
            if (this.candyTimer == 40) {
                BehaviorUtils.throwItem(owner, this.candyStack.copy(), this.player.position());
                BellHelper.setCandyTimer(owner);
                if (this.player instanceof ServerPlayer serverPlayer) {
                    ModTriggers.HALLOWEEN.trigger(serverPlayer, owner, this.candyStack.copy());
                }
                owner.getBrain().eraseMemory(ModMemories.candyTarget);
                owner.getBrain().setActiveActivityIfPossible(Activity.REST);
            }
        }
    }

    @Override
    protected void stop(@Nonnull ServerLevel level, @Nonnull Villager entity, long gameTime) {
        this.player = null;
        this.target = null;
        this.candyTimer = 0;
        this.candyStack = null;
        entity.getBrain().eraseMemory(ModMemories.candyTarget);
        entity.getBrain().setActiveActivityIfPossible(Activity.REST);
    }

    @Override
    protected boolean canStillUse(@Nonnull ServerLevel level, @Nonnull Villager entity, long gameTime) {
        return this.player != null && this.target != null;
    }
}
