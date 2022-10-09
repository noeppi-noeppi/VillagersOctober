package io.github.noeppi_noeppi.mods.villagersoctober.structure;

import io.github.noeppi_noeppi.mods.villagersoctober.ModBlocks;
import io.github.noeppi_noeppi.mods.villagersoctober.ModItems;
import io.github.noeppi_noeppi.mods.villagersoctober.bloon.GhastBalloon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Random;

public class VillageDecorator {
    
    public static void decorate(WorldGenLevel level, ResourceKey<Structure> structure, StructureTemplate template, StructurePlaceSettings oldSettings, BlockPos at, BlockPos at2) {
        StructurePlaceSettings settings = oldSettings.copy();
        settings.clearProcessors();
        settings.setRandom(null);
        StructureTemplate.Palette palette = settings.getRandomPalette(template.palettes, at);
        Random random = new Random(Mth.getSeed(at) ^ ((((long) "Villager".hashCode()) << 32) | "October".hashCode()));
        
        boolean hasScarecrow = false;
        
        for (StructureTemplate.StructureBlockInfo block : TemplateHelper.processBlockInfos(level, at, at2, settings, palette.blocks(), template)) {
            if (!hasScarecrow && block.state.is(BlockTags.CROPS) && random.nextInt(15) == 0) {
                if (level.getBlockState(block.pos).is(BlockTags.CROPS) && level.isEmptyBlock(block.pos.above())) {
                    if (level.getBlockState(block.pos.below()).getBlock() == Blocks.FARMLAND) {
                        level.setBlock(block.pos.below(), Blocks.COARSE_DIRT.defaultBlockState(), 3);
                    }
                    level.setBlock(block.pos, ModBlocks.scarecrow.defaultBlockState().setValue(BlockStateProperties.ROTATION_16, random.nextInt(16)), 3);
                    level.setBlock(block.pos.above(), ModBlocks.scarecrowTop.defaultBlockState(), 3);
                    hasScarecrow = true;
                }
            }
            
            if (block.state.is(BlockTags.DOORS) && block.state.getBlock() instanceof DoorBlock && block.state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
                if (level.getBlockState(block.pos).is(BlockTags.DOORS)) {
                    Direction facing = block.state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    BlockPos frontPos = block.pos.relative(facing.getOpposite());
                    Direction firstLook = block.state.getValue(BlockStateProperties.DOOR_HINGE) == DoorHingeSide.LEFT ? facing.getClockWise() : facing.getCounterClockWise();
                    if (!tryPlaceBell(level, frontPos.relative(firstLook), facing)) {
                        tryPlaceBell(level, frontPos.relative(firstLook.getOpposite()), facing);
                    }
                }
            }
            
            if (block.state.is(BlockTags.SMALL_FLOWERS) && random.nextInt(4) == 0) {
                if (level.getBlockState(block.pos).is(BlockTags.SMALL_FLOWERS)) {
                    Direction dir = Direction.from2DDataValue(random.nextInt(4));
                    if (level.isEmptyBlock(block.pos.relative(dir))) {
                        level.setBlock(block.pos, Blocks.JACK_O_LANTERN.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, dir), 3);
                    }
                }
            }
            
            if (block.state.is(BlockTags.FENCES) && random.nextInt(40) == 0) {
                if (level.getBlockState(block.pos).is(BlockTags.FENCES)) {
                    if (level.isEmptyBlock(block.pos.above(1)) && level.isEmptyBlock(block.pos.above(2)) && level.isEmptyBlock(block.pos.above(3))) {
                        GhastBalloon balloon = ModItems.ghastBalloon.entityType.create(level.getLevel());
                        if (balloon != null) {
                            balloon.setPos(block.pos.getX() + 0.5, block.pos.getY() + 2.6, block.pos.getZ() + 0.5);
                            balloon.setLookAngleDeg(random.nextFloat() * 360);
                            balloon.setTiePos(block.pos.immutable());
                            trySpawnEntity(level, balloon);
                        }
                    }
                }
            }
        }
    }
    
    private static boolean tryPlaceBell(WorldGenLevel level, BlockPos pos, Direction facing) {
        BlockState currentState = level.getBlockState(pos);
        if (currentState.getBlock() == ModBlocks.doorbell) return true;
        if (!currentState.isAir() && currentState.getBlock() != Blocks.TORCH && !currentState.is(BlockTags.SMALL_FLOWERS)) return false;
        if (!level.getBlockState(pos.relative(facing)).isFaceSturdy(level, pos, facing.getOpposite())) return false;
        if (level.getBlockState(pos.relative(facing)).is(BlockTags.DOORS)) return false;
        BlockState state = ModBlocks.doorbell.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
        level.setBlock(pos, state, 3);
        return true;
    }
    
    private static void trySpawnEntity(WorldGenLevel level, Entity entity) {
        if (level instanceof WorldGenRegion wgr) {
            int x = ((int) Math.floor(entity.getX())) >> 4;
            int z = ((int) Math.floor(entity.getZ())) >> 4;
            if (wgr.getCenter().x == x && wgr.getCenter().z == z) {
                level.addFreshEntity(entity);
            }
        } else {
            level.addFreshEntity(entity);
        }
    }
}
