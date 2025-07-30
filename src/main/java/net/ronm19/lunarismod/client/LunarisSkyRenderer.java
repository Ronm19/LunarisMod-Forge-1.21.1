package net.ronm19.lunarismod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class LunarisSkyRenderer extends DimensionSpecialEffects {
    private static final ResourceLocation MOON_HALO =
            ResourceLocation.fromNamespaceAndPath("lunarismod", "textures/environment/moon_halo_purple.png");

    public LunarisSkyRenderer() {
        super(192.0F, true, SkyType.NORMAL, false, false);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 color, float brightness) {
        return color;
    }

    @Override
    public boolean isFoggyAt(int x, int z) {
        return false;
    }

    @Override
    public boolean renderSky(
            ClientLevel level,
            int ticks,
            float partialTicks,
            Camera camera,
            Matrix4f projectionMatrix,
            boolean darkenSky,
            Runnable vanillaRenderer
    ) {
        // 🌌 Render the default sky (sun, moon, stars)
        vanillaRenderer.run();

        // 🌕 Compute moon position in sky dome
        float angle = level.getTimeOfDay(partialTicks);
        float moonX = Mth.cos(angle * Mth.TWO_PI) * 100.0F;
        float moonY = Mth.sin(angle * Mth.TWO_PI) * 100.0F;
        float moonZ = -120.0F;

        // 🌙 Pulse the glow over time
        float pulse = 1.0F + 0.05F * Mth.sin((level.getDayTime() % 24000L) / 24000F * Mth.TWO_PI);
        float scale = 20.0F * pulse;

        // 🎨 Setup transform for halo quad
        PoseStack poseStack = new PoseStack();
        poseStack.translate(moonX, moonY, moonZ);
        poseStack.scale(scale, scale, scale);

        var pose = poseStack.last().pose();
        var normal = poseStack.last().normal();

        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        textureManager.bindForSetup(MOON_HALO);

        MultiBufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vc = buffer.getBuffer(RenderType.text(MOON_HALO));

        Matrix4f poseMatrix = poseStack.last().pose();
        var notify = poseStack.last().normal();

// Vertex 1
        vc.addVertex(poseMatrix, -0.5F,  0.5F, 0F);
        vc.setColor(255, 255, 255, 128);
        vc.setUv(1F, 1F);
        vc.setColor(0);
        vc.setUv2(0xF000F0, 3);
        vc.notify();
        VertexConsumer vertexConsumer = vc.addVertex(-0.5F,  0.5F, 0F);

// Vertex 2
        vc.addVertex(poseMatrix, 0.5F,  0.5F, 0F);
        vc.setColor(255, 255, 255, 128);
        vc.setUv(1F, 1F);
        vc.setColor(0);
        vc.setUv2(0xF000F0, 1);
        vc.notify();
        vertexConsumer = vc.addVertex(0.5F,  0.5F, 0F);

// Vertex 3
        vc.addVertex(poseMatrix,  0.5F, -0.5F, 0F);
        vc.setColor(255, 255, 255, 128);
        vc.setUv(1F, 1F);
        vc.setColor(0);
        vc.setUv2(0xF000F0, 3);
        vc.notify();
        vertexConsumer = vc.addVertex(0.5F, -0.5F, 0F);

// Vertex 4
        vc.addVertex(poseMatrix, -0.5F, -0.5F, 0F);
        vc.setColor(255, 255, 255, 128);
        vc.setUv(1F, 1F);
        vc.setColor(0);
        vc.setUv2(0xF000F0, 2);
        vc.notify();
        vertexConsumer = vc.addVertex(-0.5F, -0.5F, 0F);

        // 🧼 Optional: flush buffers manually
        // buffers.endBatch(); // only if you want to enforce halo draw immediately
        return darkenSky;
    }
}