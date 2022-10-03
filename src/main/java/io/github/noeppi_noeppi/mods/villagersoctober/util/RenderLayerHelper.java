package io.github.noeppi_noeppi.mods.villagersoctober.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.function.Function;

public class RenderLayerHelper {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void addPlayerLayer(Function<RenderLayerParent<Player, PlayerModel<Player>>, RenderLayer<Player, PlayerModel<Player>>> factory) {
        try {
            Map<String, EntityRenderer<? extends Player>> skinMap = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();
            if (skinMap.get("default") instanceof LivingEntityRenderer render) render.addLayer(factory.apply((RenderLayerParent<Player, PlayerModel<Player>>) render));
            if (skinMap.get("slim") instanceof LivingEntityRenderer render) render.addLayer(factory.apply((RenderLayerParent<Player, PlayerModel<Player>>) render));
        } catch (Exception e) {
            // Just in case
            e.printStackTrace();
        }
    }
}
