"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var coremods_1 = require("coremods");
function initializeCoreMod() {
    return {
        structure_track: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement',
                methodName: 'm_213695_',
                methodDesc: '(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Rotation;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/util/RandomSource;Z)Z'
            },
            transformer: function (method) {
                var lvtTemplate = -2;
                var lvtSettings = -2;
                var insertBefore = null;
                for (var i = 0; i < method.instructions.size(); i++) {
                    var insn = method.instructions.get(i);
                    if (insn == null)
                        continue;
                    if (lvtTemplate == -2 && insn.getOpcode() == coremods_1.Opcodes.INVOKEVIRTUAL) {
                        var methodInsn = insn;
                        if (methodInsn.owner == 'net/minecraft/world/level/levelgen/structure/pools/SinglePoolElement' && methodInsn.name == coremods_1.ASMAPI.mapMethod('m_227299_') && methodInsn.desc == '(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;') {
                            lvtTemplate = -1;
                        }
                    }
                    if (lvtSettings == -2 && insn.getOpcode() == coremods_1.Opcodes.INVOKEVIRTUAL) {
                        var methodInsn = insn;
                        if (methodInsn.owner == 'net/minecraft/world/level/levelgen/structure/pools/SinglePoolElement' && methodInsn.name == coremods_1.ASMAPI.mapMethod('m_207169_') && methodInsn.desc == '(Lnet/minecraft/world/level/block/Rotation;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Z)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;') {
                            lvtSettings = -1;
                        }
                    }
                    if (lvtTemplate == -1 && insn.getOpcode() == coremods_1.Opcodes.ASTORE) {
                        lvtTemplate = insn.var;
                    }
                    if (lvtSettings == -1 && insn.getOpcode() == coremods_1.Opcodes.ASTORE) {
                        lvtSettings = insn.var;
                    }
                    if (insn.getOpcode() == coremods_1.Opcodes.IRETURN) {
                        // Will find the last one
                        insertBefore = insn;
                    }
                }
                if (lvtTemplate < 0 || lvtSettings < 0 || insertBefore == null) {
                    throw new Error('Failed to patch SinglePoolElement#place');
                }
                var target = new coremods_1.InsnList();
                target.add(new coremods_1.VarInsnNode(coremods_1.Opcodes.ALOAD, 2));
                target.add(new coremods_1.VarInsnNode(coremods_1.Opcodes.ALOAD, lvtTemplate));
                target.add(new coremods_1.VarInsnNode(coremods_1.Opcodes.ALOAD, lvtSettings));
                target.add(new coremods_1.VarInsnNode(coremods_1.Opcodes.ALOAD, 5));
                target.add(new coremods_1.VarInsnNode(coremods_1.Opcodes.ALOAD, 6));
                target.add(new coremods_1.MethodInsnNode(coremods_1.Opcodes.INVOKESTATIC, 'io/github/noeppi_noeppi/mods/villagersoctober/core/CoreModUtil', 'afterPlacedTemplate', '(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)V'));
                method.instructions.insertBefore(insertBefore, target);
                return method;
            }
        }
    };
}
