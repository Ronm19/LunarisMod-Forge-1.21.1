package net.ronm19.lunarismod.client;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class FogHandler {
    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        if (event.getCamera().getEntity() instanceof Player player) {
            if (ClientEffectManager.shouldApplyLunarFog(player)) {
                event.setNearPlaneDistance(0.05f);
                event.setFarPlaneDistance(12f); // Thicker fog
                // Optional: add color or sound handling elsewhere
            }
        }
    }
}