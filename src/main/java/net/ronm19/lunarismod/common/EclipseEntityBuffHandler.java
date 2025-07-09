package net.ronm19.lunarismod.common;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ronm19.lunarismod.client.EclipseClientState;
import net.ronm19.lunarismod.entity.ModEntities;

@Mod.EventBusSubscriber
public class EclipseEntityBuffHandler {

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.level.isClientSide()) return;

        Level level = event.level;
        AABB searchArea = new AABB(level.getSharedSpawnPos()).inflate(128);

        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, searchArea)) {
            if (isEclipseCreature(entity) && entity instanceof Mob mob) {
                if (EclipseClientState.isEclipseActive()) {
                    applyEclipseBuffs(mob);
                } else {
                    removeEclipseBuffs(mob);
                }
            }
        }
    }

    private static boolean isEclipseCreature(LivingEntity entity) {
        return entity.getType() == ModEntities.LUNARWOLF.get()
                || entity.getType() == ModEntities.VOIDHOWLER.get();
    }

    private static void applyEclipseBuffs(Mob mob) {
        int duration = 300; // ticks, 3 seconds
        mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, 1, true, false));
        mob.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, duration, 2, true, false));
        mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, 1, true, false));
        mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, 0, true, false));
        mob.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, duration, 0, true, false));
    }

    private static void removeEclipseBuffs(Mob mob) {
        mob.removeEffect(MobEffects.DAMAGE_BOOST);
        mob.removeEffect(MobEffects.HEALTH_BOOST);
        mob.removeEffect(MobEffects.MOVEMENT_SPEED);
        mob.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        mob.removeEffect(MobEffects.FIRE_RESISTANCE);
    }
}
