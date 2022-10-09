"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var coremods_1 = require("coremods");
function initializeCoreMod() {
    return {
        wrap: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.entity.npc.Villager',
                methodName: 'm_5490_',
                methodDesc: '()Lnet/minecraft/world/entity/ai/Brain$Provider;'
            },
            transformer: function (method) {
                var target = new coremods_1.InsnList();
                target.add(new coremods_1.MethodInsnNode(coremods_1.Opcodes.INVOKESTATIC, 'io/github/noeppi_noeppi/mods/villagersoctober/core/CoreModUtil', 'wrapVillagerBrain', '(Lnet/minecraft/world/entity/ai/Brain$Provider;)Lnet/minecraft/world/entity/ai/Brain$Provider;'));
                for (var i = method.instructions.size() - 1; i >= 0; i--) {
                    var insn = method.instructions.get(i);
                    if (insn != null && insn.getOpcode() == coremods_1.Opcodes.ARETURN) {
                        method.instructions.insertBefore(insn, target);
                        break;
                    }
                }
                return method;
            }
        },
        init_brain: {
            target: {
                type: 'METHOD',
                class: 'net.minecraft.world.entity.npc.Villager',
                methodName: 'm_35424_',
                methodDesc: '(Lnet/minecraft/world/entity/ai/Brain;)V'
            },
            transformer: function (method) {
                var target = new coremods_1.InsnList();
                target.add(new coremods_1.VarInsnNode(coremods_1.Opcodes.ALOAD, 0));
                target.add(new coremods_1.VarInsnNode(coremods_1.Opcodes.ALOAD, 1));
                target.add(new coremods_1.MethodInsnNode(coremods_1.Opcodes.INVOKESTATIC, 'io/github/noeppi_noeppi/mods/villagersoctober/core/CoreModUtil', 'initVillagerBrain', '(Lnet/minecraft/world/entity/npc/Villager;Lnet/minecraft/world/entity/ai/Brain;)V'));
                method.instructions.insert(target);
                return method;
            }
        }
    };
}
