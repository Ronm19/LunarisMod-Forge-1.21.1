package net.ronm19.lunarismod.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties {
    public static final FoodProperties MOON_FRUIT_STEW = new FoodProperties.Builder().nutrition(6).saturationModifier(0.8f)
            .effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 3600), 1.0f).alwaysEdible().build();

    public static final FoodProperties MOONPPLE = new FoodProperties.Builder().nutrition(7).saturationModifier(1.0f)
            .effect(new MobEffectInstance(MobEffects.REGENERATION, 7200), 1.0f).alwaysEdible().build();

}