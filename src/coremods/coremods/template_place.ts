import { AbstractInsnNode, ASMAPI, CoreMods, InsnList, MethodInsnNode, MethodNode, Opcodes, VarInsnNode } from "coremods";

function initializeCoreMod(): CoreMods {
    return {
        structure_place: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement',
                methodName: 'm_213695_',
                methodDesc: '(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Rotation;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/util/RandomSource;Z)Z'
            },
            transformer: (method: MethodNode) => {
                let lvtTemplate = -2
                let lvtSettings = -2
                let insertBefore: AbstractInsnNode | null = null

                for (let i = 0; i < method.instructions.size(); i++) {
                    const insn = method.instructions.get(i)
                    if (insn == null) continue
                    if (lvtTemplate == -2 && insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                        const methodInsn = insn as MethodInsnNode
                        if (methodInsn.owner == 'net/minecraft/world/level/levelgen/structure/pools/SinglePoolElement' && methodInsn.name == ASMAPI.mapMethod('m_227299_') && methodInsn.desc == '(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;') {
                            lvtTemplate = -1
                        }
                    }

                    if (lvtSettings == -2 && insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                        const methodInsn = insn as MethodInsnNode
                        if (methodInsn.owner == 'net/minecraft/world/level/levelgen/structure/pools/SinglePoolElement' && methodInsn.name == ASMAPI.mapMethod('m_207169_') && methodInsn.desc == '(Lnet/minecraft/world/level/block/Rotation;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Z)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;') {
                            lvtSettings = -1
                        }
                    }

                    if (lvtTemplate == -1 && insn.getOpcode() == Opcodes.ASTORE) {
                        lvtTemplate = (insn as VarInsnNode).var
                    }

                    if (lvtSettings == -1 && insn.getOpcode() == Opcodes.ASTORE) {
                        lvtSettings = (insn as VarInsnNode).var
                    }

                    if (insn.getOpcode() == Opcodes.IRETURN && insn.getPrevious()?.getOpcode() == Opcodes.ICONST_1) {
                        insertBefore = insn
                    }
                }

                if (lvtTemplate < 0 || lvtSettings < 0 || insertBefore == null) {
                    throw new Error('Failed to patch SinglePoolElement#place')
                }

                const target = new InsnList()

                target.add(new VarInsnNode(Opcodes.ALOAD, 2))
                target.add(new VarInsnNode(Opcodes.ALOAD, lvtTemplate))
                target.add(new VarInsnNode(Opcodes.ALOAD, lvtSettings))
                target.add(new VarInsnNode(Opcodes.ALOAD, 5))
                target.add(new VarInsnNode(Opcodes.ALOAD, 6))
                target.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                    'io/github/noeppi_noeppi/mods/villagersoctober/core/CoreModUtil',
                    'afterPlacedTemplate', '(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)V'
                ))

                method.instructions.insertBefore(insertBefore, target)

                return method
            }
        }
    }
}
