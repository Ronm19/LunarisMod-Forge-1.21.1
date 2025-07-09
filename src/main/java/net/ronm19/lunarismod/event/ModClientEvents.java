package net.ronm19.lunarismod.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.ronm19.lunarismod.LunarisMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.ronm19.lunarismod.client.LunarisSkyRenderer;

import java.lang.reflect.Field;
import java.util.Map;


@Mod.EventBusSubscriber(modid = LunarisMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent
    public static void onClientSetup( FMLClientSetupEvent event ) {
        event.enqueueWork(() -> {
            try {
                Field effectsField = DimensionSpecialEffects.class.getDeclaredField("EFFECTS");
                effectsField.setAccessible(true);
                Map<ResourceLocation, DimensionSpecialEffects> effects = (Map<ResourceLocation, DimensionSpecialEffects>) effectsField.get(null);
                effects.put(Level.OVERWORLD.location(), new LunarisSkyRenderer());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
