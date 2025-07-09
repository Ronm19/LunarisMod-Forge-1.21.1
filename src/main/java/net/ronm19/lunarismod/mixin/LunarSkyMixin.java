package net.ronm19.lunarismod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.ronm19.lunarismod.client.EclipseClientState;

@Mixin(LevelRenderer.class)
public class LunarSkyMixin {

    private static final ResourceLocation CUSTOM_MOON =
            ResourceLocation.fromNamespaceAndPath("lunarismod", "textures/environment/moon_phases_purple.png");

    @Inject(method = "renderSky", at = @At("TAIL"))
    private void injectPurpleMoon( Matrix4f pFrustumMatrix, Matrix4f pProjectionMatrix, float pPartialTick, Camera pCamera, boolean pIsFoggy, Runnable pSkyFogSetup, CallbackInfo ci ) {

        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;

        if (level == null || !level.dimension().equals(Level.OVERWORLD)) return;
        if (!EclipseClientState.isEclipseActive()) return;

        GuiGraphics graphics = new GuiGraphics(mc, mc.renderBuffers().bufferSource());

        int size = 40;
        int x = mc.getWindow().getGuiScaledWidth() / 2 - size / 2;
        int y = mc.getWindow().getGuiScaledHeight() / 5;

        graphics.blit(CUSTOM_MOON, x, y, 0, 0, size, size, size, size);
    }
}
