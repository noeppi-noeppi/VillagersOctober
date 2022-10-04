"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var coremods_1 = require("coremods");
function initializeCoreMod() {
    return {
        hide_head: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.client.renderer.entity.layers.CustomHeadLayer',
                methodName: 'm_6494_',
                methodDesc: '(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V'
            },
            transformer: function (method) {
                var target = new coremods_1.InsnList();
                var label = new coremods_1.LabelNode();
                target.add(new coremods_1.VarInsnNode(coremods_1.Opcodes.ALOAD, 4));
                target.add(new coremods_1.TypeInsnNode(coremods_1.Opcodes.CHECKCAST, 'net/minecraft/world/entity/Entity'));
                target.add(new coremods_1.MethodInsnNode(coremods_1.Opcodes.INVOKESTATIC, 'io/github/noeppi_noeppi/mods/villagersoctober/core/CoreModUtil', 'shouldRenderHead', '(Lnet/minecraft/world/entity/Entity;)Z'));
                target.add(new coremods_1.JumpInsnNode(coremods_1.Opcodes.IFNE, label));
                target.add(new coremods_1.InsnNode(coremods_1.Opcodes.RETURN));
                target.add(label);
                method.instructions.insert(target);
                return method;
            }
        }
    };
}
