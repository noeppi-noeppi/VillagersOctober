import { CoreMods, InsnList, InsnNode, JumpInsnNode, LabelNode, MethodInsnNode, MethodNode, Opcodes, TypeInsnNode, VarInsnNode } from "coremods";

function initializeCoreMod(): CoreMods {
    return {
        wrap: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.entity.npc.Villager',
                methodName: 'm_5490_',
                methodDesc: '()Lnet/minecraft/world/entity/ai/Brain$Provider;'
            },
            transformer: (method: MethodNode) => {
                const target = new InsnList()

                target.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                    'io/github/noeppi_noeppi/mods/villagersoctober/core/CoreModUtil',
                    'wrapVillagerBrain', '(Lnet/minecraft/world/entity/ai/Brain$Provider;)Lnet/minecraft/world/entity/ai/Brain$Provider;'
                ))

                for (let i = method.instructions.size() - 1; i >= 0; i--) {
                    const insn = method.instructions.get(i)
                    if (insn != null && insn.getOpcode() == Opcodes.ARETURN) {
                        method.instructions.insertBefore(insn, target)
                        break
                    }
                }

                return method
            }
        },
        init_brain: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.entity.npc.Villager',
                methodName: 'm_35424_',
                methodDesc: '(Lnet/minecraft/world/entity/ai/Brain;)V'
            },
            transformer: (method: MethodNode) => {
                const target = new InsnList()

                target.add(new VarInsnNode(Opcodes.ALOAD, 0))
                target.add(new VarInsnNode(Opcodes.ALOAD, 1))
                target.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                    'io/github/noeppi_noeppi/mods/villagersoctober/core/CoreModUtil',
                    'initVillagerBrain', '(Lnet/minecraft/world/entity/npc/Villager;Lnet/minecraft/world/entity/ai/Brain;)V'
                ))

                method.instructions.insert(target)

                return method
            }
        }
    }
}
