import { CoreMods, InsnList, InsnNode, JumpInsnNode, LabelNode, MethodInsnNode, MethodNode, Opcodes, TypeInsnNode, VarInsnNode } from "coremods";

function initializeCoreMod(): CoreMods {
    return {
        hide_head: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.client.renderer.entity.layers.CustomHeadLayer',
                methodName: 'm_6494_',
                methodDesc: '(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V'
            },
            transformer: (method: MethodNode) => {
                const target = new InsnList()
                const label = new LabelNode()

                target.add(new VarInsnNode(Opcodes.ALOAD, 4))
                target.add(new TypeInsnNode(Opcodes.CHECKCAST, 'net/minecraft/world/entity/Entity'))
                target.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                    'io/github/noeppi_noeppi/mods/villagersoctober/core/CoreModUtil',
                    'shouldRenderHead', '(Lnet/minecraft/world/entity/Entity;)Z'
                ))
                target.add(new JumpInsnNode(Opcodes.IFNE, label))
                target.add(new InsnNode(Opcodes.RETURN))
                target.add(label)

                method.instructions.insert(target)

                return method
            }
        }
    }
}
