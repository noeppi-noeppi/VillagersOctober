import { CoreMods, InsnList, MethodInsnNode, MethodNode, Opcodes, VarInsnNode } from "coremods";

function initializeCoreMod(): CoreMods {
    return {
        structure_track: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.level.levelgen.structure.StructureStart',
                methodName: 'm_226850_',
                methodDesc: '(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/world/level/ChunkPos;)V'
            },
            transformer: (method: MethodNode) => {
                const targetStart = new InsnList()
                targetStart.add(new VarInsnNode(Opcodes.ALOAD, 0))
                targetStart.add(new VarInsnNode(Opcodes.ALOAD, 1))
                targetStart.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                    'io/github/noeppi_noeppi/mods/villagersoctober/core/StructureTracker',
                    'startStructure', '(Lnet/minecraft/world/level/levelgen/structure/StructureStart;Lnet/minecraft/world/level/WorldGenLevel;)V'
                ))
                
                const targetEnd = new InsnList()
                targetEnd.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                    'io/github/noeppi_noeppi/mods/villagersoctober/core/StructureTracker',
                    'endStructure', '()V'
                ))

                for (let i = method.instructions.size() - 1; i >= 0; i--) {
                    const insn = method.instructions.get(i)
                    if (insn != null && insn.getOpcode() == Opcodes.RETURN) {
                        method.instructions.insertBefore(insn, targetEnd)
                        break
                    }
                }

                method.instructions.insert(targetStart)

                return method
            }
        }
    }
}
