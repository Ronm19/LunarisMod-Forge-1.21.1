package net.ronm19.lunarismod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ronm19.lunarismod.entity.ModEntities;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EclipseOverlayRenderer {
    private static final double RADIUS = 55.0;
    private static final long SOUND_INTERVAL = 100;
    private static final long PARTICLE_INTERVAL = 5;

    private static long nextSoundTime = 0;
    private static long nextParticleTime = 0;

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        if (!shouldShowEclipseEffects()) return;
        event.setCanceled(true);
        event.setNearPlaneDistance(0.0F);
        event.setFarPlaneDistance(192.0F);
    }

    @SubscribeEvent
    public static void onComputeFogColor(ViewportEvent.ComputeFogColor event) {
        if (!shouldShowEclipseEffects()) return;
        event.setRed(0.4F);
        event.setGreen(0.0F);
        event.setBlue(0.4F);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!shouldShowEclipseEffects()) return;

        spawnEclipseParticles();
        playEclipseSound();
    }

    private static boolean shouldShowEclipseEffects() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        Level world = mc.level;
        if (mc.isPaused() || player == null || world == null) return false;

        long dayTime = world.getDayTime() % 24000;
        if (dayTime < 12000 || world.getMoonBrightness() < 1.0F) return false;

        List<LivingEntity> nearby = world.getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(RADIUS)
        );

        for (LivingEntity e : nearby) {
            if (e.getType() == ModEntities.LUNARWOLF.get() || e.getType() == ModEntities.VOIDHOWLER.get()) {
                return true;
            }
        }
        return false;
    }

    private static void spawnEclipseParticles() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        Level world = mc.level;
        if (player == null || world == null) return;

        long now = world.getGameTime();
        if (now < nextParticleTime) return;
        nextParticleTime = now + PARTICLE_INTERVAL;

        List<LivingEntity> entities = world.getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(RADIUS),
                e -> e.getType() == ModEntities.LUNARWOLF.get() || e.getType() == ModEntities.VOIDHOWLER.get()
        );

        for (LivingEntity e : entities) {
            double dx = e.getX() + (world.random.nextDouble() - 0.5) * 1.0;
            double dy = e.getY() + 0.5 + world.random.nextDouble();
            double dz = e.getZ() + (world.random.nextDouble() - 0.5) * 1.0;
            world.addParticle(ParticleTypes.END_ROD, dx, dy, dz, 0.0, 0.01, 0.0);
        }
    }

    private static void playEclipseSound() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        Level world = mc.level;
        if (player == null || world == null) return;

        long now = world.getGameTime();
        if (now >= nextSoundTime) {
            player.playSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP.value(), 0.6F, 0.9F);
            nextSoundTime = now + SOUND_INTERVAL;
        }
    }
}