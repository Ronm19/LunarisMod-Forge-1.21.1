package net.ronm19.lunarismod.client;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ronm19.lunarismod.entity.ModEntities;

@Mod.EventBusSubscriber(modid = "lunarismod")
public class MoonBuffHandler {

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        Level level = event.level;

        // Only server-side during full moon night
        if (level.isClientSide || !level.isNight() || level.getMoonBrightness() < 1.0F) return;

        for (LivingEntity mob : level.getEntitiesOfClass(LivingEntity.class, level.getWorldBorder().getCollisionShape().bounds())) {
            EntityType<?> type = mob.getType();
            if (type == ModEntities.LUNARWOLF.get() || type == ModEntities.VOIDHOWLER.get()) {
                applyBuff(mob, MobEffects.DAMAGE_BOOST, 220, 4);   // Strength V
                applyBuff(mob, MobEffects.ABSORPTION,   220, 1);   // Absorption II
                applyBuff(mob, MobEffects.MOVEMENT_SPEED, 220, 2); // Speed III
            }
        }
    }

    private static void applyBuff(LivingEntity mob, Holder<MobEffect> effectHolder, int duration, int amplifier) {
        MobEffectInstance current = mob.getEffect(effectHolder);
        if (current == null || current.getAmplifier() < amplifier || current.getDuration() < 100) {
            mob.addEffect(new MobEffectInstance(effectHolder, duration, amplifier, false, false));
        }
    }
}