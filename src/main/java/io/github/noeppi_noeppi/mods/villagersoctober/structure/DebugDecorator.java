package io.github.noeppi_noeppi.mods.villagersoctober.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

@SuppressWarnings("unused")
public class DebugDecorator {

    public static void decorate(WorldGenLevel level, ResourceKey<Structure> structure, StructureTemplate template, StructurePlaceSettings oldSettings, BlockPos at, BlockPos at2) {
        StructurePlaceSettings settings = oldSettings.copy();
        settings.clearProcessors();
        settings.setRandom(null);
        StructureTemplate.Palette palette = settings.getRandomPalette(template.palettes, at);

        Map<BlockPos, List<ItemStack>> stackMap = new HashMap<>();
        
        for (StructureTemplate.StructureBlockInfo block : TemplateHelper.processBlockInfos(level, at, at2, settings, palette.blocks(), template)) {
            addBlockDebug(level, block, stackMap);
        }
        
        placeBlockDebug(level, stackMap);
    }
    
    private static void addBlockDebug(WorldGenLevel level, StructureTemplate.StructureBlockInfo block, Map<BlockPos, List<ItemStack>> stackMap) {
        if (block.state.isAir()) return;
        BlockPos pos = block.pos.immutable();
        if (level.isEmptyBlock(block.pos)) return;
        if (level.getBlockState(block.pos).getBlock() == Blocks.BARREL) return;

        CompoundTag nbt = new CompoundTag();
        nbt.putString("title", "Block Info - " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
        nbt.putString("author", "Minecraft");
        List<String> lines = new ArrayList<>();

        lines.add("T: " + stateStr(block.state) + "\nW: " + stateStr(level.getBlockState(pos))
                + "\nU+Y: " + stateStr(level.getBlockState(pos.above()))
                + "\nD-Y: " + stateStr(level.getBlockState(pos.below()))
                + "\nE+X: " + stateStr(level.getBlockState(pos.east()))
                + "\nW-X: " + stateStr(level.getBlockState(pos.west()))
                + "\nS+Z: " + stateStr(level.getBlockState(pos.south()))
                + "\nN-Z: " + stateStr(level.getBlockState(pos.north()))
        );

        int counter = 0;
        StringBuilder current = new StringBuilder();
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (!current.isEmpty()) current.append("\n");
                    String id = (x > 0 ? "+" : "") + x + (y > 0 ? "+" : "") + y + (z >= 0 ? "+" : "") + z;
                    current.append(id).append(": ").append(stateStr(level.getBlockState(pos.offset(x, y, z))));
                    if ((++counter) >= 7) {
                        counter = 0;
                        lines.add(current.toString());
                        current = new StringBuilder();
                    }
                }
            }
        }
        if (!current.isEmpty()) {
            lines.add(current.toString());
        }

        ListTag list = new ListTag();
        lines.forEach(l -> list.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal(l)))));
        nbt.put("pages", list);

        ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
        stack.setTag(nbt);

        stackMap.computeIfAbsent(pos, k -> new ArrayList<>()).add(stack);
    }

    private static String stateStr(BlockState state) {
        ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(state.getBlock());
        if (rl == null) return "UNKNOWN";
        StringBuilder sb = new StringBuilder();
        if (!rl.getNamespace().equals("minecraft")) sb.append(rl.getNamespace()).append(":");
        sb.append(rl.getPath());
        if (!state.getProperties().isEmpty()) {
            sb.append("[");
            boolean first = true;
            for (Property<?> prop : state.getProperties()) {
                if (!first) sb.append(",");
                first = false;
                sb.append(prop.getName().charAt(0)).append("=");
                Object value = state.getValue(prop);
                if (value instanceof Direction dir) {
                    sb.append(dir.name().toUpperCase(Locale.ROOT).charAt(0));
                } else if (value instanceof Direction.Axis axis) {
                    sb.append(axis.name().toUpperCase(Locale.ROOT).charAt(0));
                } else {
                    String valueStr = value.toString();
                    sb.append(valueStr.isEmpty() ? "?" : valueStr.substring(0, 1));
                }
            }
            sb.append("]");
        }
        return sb.toString();
    }
    
    private static void placeBlockDebug(WorldGenLevel level, Map<BlockPos, List<ItemStack>> stackMap) {
        for (Map.Entry<BlockPos, List<ItemStack>> entry : stackMap.entrySet()) {
            level.setBlock(entry.getKey(), Blocks.BARREL.defaultBlockState(), 3);
            BlockEntity blockEntity = level.getBlockEntity(entry.getKey());
            if (blockEntity instanceof BarrelBlockEntity chest) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    chest.setItem(i, entry.getValue().get(i).copy());
                }
            } else {
                level.setBlock(entry.getKey(), Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
            }
        }
    }
}
