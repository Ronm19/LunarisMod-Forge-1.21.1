package net.ronm19.lunarismod.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ronm19.lunarismod.LunarisMod;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, LunarisMod.MOD_ID);

    public static final RegistryObject<MobEffect> LUNAR_EFFECT = MOB_EFFECTS.register("lunar",
            () -> new LunarEffect(MobEffectCategory.BENEFICIAL, 0x369B7CFF)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "lunar"),
                            0.35f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}