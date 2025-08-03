package net.ronm19.lunarismod.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record LunarGraceEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<LunarGraceEnchantmentEffect> CODEC = MapCodec.unit(LunarGraceEnchantmentEffect::new);

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        boolean isNight = !level.isDay();

        if (isNight) {
            if (enchantmentLevel >= 1) {
                EntityType.WITHER_SKULL.spawn(level, entity.getOnPos(), MobSpawnType.TRIGGERED);
            }
            if (enchantmentLevel >= 2) {
                EntityType.WITHER_SKULL.spawn(level, entity.getOnPos(), MobSpawnType.TRIGGERED);
                EntityType.WITHER_SKULL.spawn(level, entity.getOnPos(), MobSpawnType.TRIGGERED);
            }

        } else {
            if (enchantmentLevel >= 1) {
                entity.setRemainingFireTicks(240);
            }
            if (enchantmentLevel == 2) {
                entity.setRemainingFireTicks(300);
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}

