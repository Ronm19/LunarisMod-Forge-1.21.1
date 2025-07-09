package net.ronm19.lunarismod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ronm19.lunarismod.common.EclipseLogic;
import net.ronm19.lunarismod.entity.ModEntities;
import org.joml.Matrix4f;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;



@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EclipseOverlayRenderer {

    private static final RandomSource random = RandomSource.create();

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;
        if (!EclipseLogic.shouldRenderEclipse()) return;

        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        float partialTicks = event.getPartialTick();

        // Draw translucent purple overlay
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        GuiGraphics graphics = new GuiGraphics(mc, mc.renderBuffers().bufferSource());
        graphics.fill(0, 0, width, height, 0x80008050);

        renderPurpleMoonGlow(partialTicks);

        RenderSystem.disableBlend();
    }

    private static void renderPurpleMoonGlow(float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        float skyAngle = mc.level.getTimeOfDay(partialTicks) * 360.0F;
        double radius = 16.0;
        float moonX = (float) (Math.cos(Math.toRadians(skyAngle)) * radius);
        float moonY = (float) (Math.sin(Math.toRadians(skyAngle)) * radius);
        float z = -100.0F;

        PoseStack poseStack = new PoseStack();
        Matrix4f matrix = poseStack.last().pose();

        Tesselator tesselator = Tesselator.getInstance();

        int r = 128, g = 0, b = 128, a = 160;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EclipseLogic.tickClient();
            spawnEclipseParticles();
        }
    }

    private static void spawnEclipseParticles() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || !EclipseLogic.shouldRenderEclipse()) return;

        Player player = mc.player;
        if (player != null) spawnParticlesAt(player);

        mc.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(64)).forEach(entity -> {
            if (isEclipseCreature(entity)) {
                spawnParticlesAt(entity);
            }
        });
    }

    private static void spawnParticlesAt(LivingEntity entity) {
        for (int i = 0; i < 3; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 1.5;
            double offsetY = random.nextDouble() * 1.5;
            double offsetZ = (random.nextDouble() - 0.5) * 1.5;

            entity.level().addParticle(
                    ParticleTypes.END_ROD,
                    entity.getX() + offsetX,
                    entity.getY() + offsetY + 0.5,
                    entity.getZ() + offsetZ,
                    0.0, 0.01, 0.0
            );
        }
    }

    private static boolean isEclipseCreature(LivingEntity entity) {
        return entity.getType() == ModEntities.LUNARWOLF.get()
                || entity.getType() == ModEntities.VOIDHOWLER.get();
    }

    @SubscribeEvent
    public static void onSkyColor(ViewportEvent.ComputeFogColor event) {
        if (!EclipseLogic.shouldRenderEclipse()) return;

        event.setRed(0.5f);
        event.setGreen(0.0f);
        event.setBlue(0.5f);
    }
}
