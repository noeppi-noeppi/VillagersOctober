"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var coremods_1 = require("coremods");
function initializeCoreMod() {
    return {
        structure_track: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.level.levelgen.structure.StructureStart',
                methodName: 'm_226850_',
                methodDesc: '(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/world/level/ChunkPos;)V'
            },
            transformer: function (method) {
                var targetStart = new coremods_1.InsnList();
                targetStart.add(new coremods_1.VarInsnNode(coremods_1.Opcodes.ALOAD, 0));
                targetStart.add(new coremods_1.VarInsnNode(coremods_1.Opcodes.ALOAD, 1));
                targetStart.add(new coremods_1.MethodInsnNode(coremods_1.Opcodes.INVOKESTATIC, 'io/github/noeppi_noeppi/mods/villagersoctober/core/StructureTracker', 'startStructure', '(Lnet/minecraft/world/level/levelgen/structure/StructureStart;Lnet/minecraft/world/level/WorldGenLevel;)V'));
                var targetEnd = new coremods_1.InsnList();
                targetEnd.add(new coremods_1.MethodInsnNode(coremods_1.Opcodes.INVOKESTATIC, 'io/github/noeppi_noeppi/mods/villagersoctober/core/StructureTracker', 'endStructure', '()V'));
                for (var i = method.instructions.size() - 1; i >= 0; i--) {
                    var insn = method.instructions.get(i);
                    if (insn != null && insn.getOpcode() == coremods_1.Opcodes.RETURN) {
                        method.instructions.insertBefore(insn, targetEnd);
                        break;
                    }
                }
                method.instructions.insert(targetStart);
                return method;
            }
        }
    };
}
