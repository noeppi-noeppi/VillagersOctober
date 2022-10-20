package io.github.noeppi_noeppi.mods.villagersoctober.structure;

import io.github.noeppi_noeppi.mods.villagersoctober.ModBlocks;
import io.github.noeppi_noeppi.mods.villagersoctober.ModItems;
import io.github.noeppi_noeppi.mods.villagersoctober.bloon.GhastBalloon;
import io.github.noeppi_noeppi.mods.villagersoctober.garland.Garland;
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
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.moddingx.libx.util.lazy.LazyValue;

import java.util.Random;

public class VillageDecorator {
    
    public static void decorate(WorldGenLevel level, ResourceKey<Structure> structure, StructureTemplate template, StructurePlaceSettings oldSettings, BlockPos at, BlockPos at2) {
        StructurePlaceSettings settings = oldSettings.copy();
        settings.clearProcessors();
        settings.setRandom(null);
        StructureTemplate.Palette palette = settings.getRandomPalette(template.palettes, at);
        Random random = new Random(Mth.getSeed(at) ^ ((((long) "Villager".hashCode()) << 32) | "October".hashCode()));
        
        boolean hasScarecrow = false;
        LazyValue<BlockPos> templateCenter = new LazyValue<>(() -> {
            BoundingBox box = template.getBoundingBox(settings, at);
            return new BlockPos(
                    Math.round((box.maxX() - box.minX()) / 2f),
                    Math.round((box.maxY() - box.minY()) / 2f),
                    Math.round((box.maxZ() - box.minZ()) / 2f)
            );
        });
        
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
                    boolean inverseDoor = false;
                    //noinspection RedundantIfStatement
                    if (BuiltinStructures.VILLAGE_DESERT.equals(structure) && block.state.getBlock() == Blocks.JUNGLE_DOOR) inverseDoor = true;
                    if (BuiltinStructures.VILLAGE_SAVANNA.equals(structure) && block.state.getBlock() == Blocks.ACACIA_DOOR) inverseDoor = true;
                    Direction facing = block.state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    if (inverseDoor) facing = facing.getOpposite();
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
            
            // Only the upper parts of buildings get garland
            if (block.pos.getY() - at.getY() >= 4 && random.nextInt(30) == 0) {
                int maxDown = Math.min(2, block.pos.getY() - at.getY() - 4);
                int xd = block.pos.getX() - templateCenter.get().getX();
                int zd = block.pos.getZ() - templateCenter.get().getZ();
                Direction.Axis firstAxis = Math.abs(xd) > Math.abs(zd) ? Direction.Axis.X : Direction.Axis.Z;
                Direction.Axis secondAxis = firstAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
                Direction first = Direction.fromAxisAndDirection(firstAxis, (firstAxis == Direction.Axis.X ? xd : zd) > 0 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE);
                Direction second = Direction.fromAxisAndDirection(secondAxis, (secondAxis == Direction.Axis.X ? xd : zd) > 0 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE);
                if (!tryPlaceGarland(level, block.pos.relative(first), first.getOpposite(), maxDown)) {
                    tryPlaceGarland(level, block.pos.relative(second), second.getOpposite(), maxDown);
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
        return level.setBlock(pos, state, 3);
    }
    
    private static boolean tryPlaceGarland(WorldGenLevel level, BlockPos pos, Direction facing, int maxDown) {
        BlockState currentState = level.getBlockState(pos);
        if (!currentState.isAir() && currentState.getBlock() != Blocks.TORCH && !currentState.is(BlockTags.SMALL_FLOWERS)) return false;
        if (!level.getBlockState(pos.relative(facing)).isFaceSturdy(level, pos, facing.getOpposite())) return false;
        if (level.getBlockState(pos.relative(facing)).is(BlockTags.DOORS)) return false;
        BlockState state = ModBlocks.garland.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
        if (!level.setBlock(pos, state, 3)) return false;
        if (level.getBlockEntity(pos) instanceof Garland garland) {
            garland.setGenerateOnLoad(maxDown);
            return true;
        } else {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            return false;
        }
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
