package io.github.noeppi_noeppi.mods.villagersoctober.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.List;

public class TemplateHelper {

    @SuppressWarnings("deprecation")
    public static List<StructureTemplate.StructureBlockInfo> processBlockInfos(LevelAccessor level, BlockPos at, BlockPos at2, StructurePlaceSettings settings, List<StructureTemplate.StructureBlockInfo> palette, @Nullable StructureTemplate template) {
        List<StructureTemplate.StructureBlockInfo> blocks = StructureTemplate.processBlockInfos(level, at, at2, settings, palette, template);
        return blocks.stream().map(block -> new StructureTemplate.StructureBlockInfo(block.pos.immutable(), block.state.mirror(settings.getMirror()).rotate(settings.getRotation()), block.nbt)).toList();
    }
}
