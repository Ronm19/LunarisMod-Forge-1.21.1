package net.ronm19.lunarismod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.ronm19.lunarismod.block.ModBlocks;
import net.ronm19.lunarismod.effect.ModEffects;
import net.ronm19.lunarismod.enchantment.ModEnchantmentEffects;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.entity.client.LunarWolfRenderer;
import net.ronm19.lunarismod.entity.client.NoctriumTomahawkProjectileRenderer;
import net.ronm19.lunarismod.entity.client.VoidHowlerRenderer;
import net.ronm19.lunarismod.item.ModCreativeModeTabs;
import net.ronm19.lunarismod.item.ModItems;
import net.ronm19.lunarismod.sound.ModSounds;
import net.ronm19.lunarismod.potion.ModPotions;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(LunarisMod.MOD_ID)
public class LunarisMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "lunarismod";
    // Directly reference a slf4j logger
   public static final Logger LOGGER = LogUtils.getLogger();

    public LunarisMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);


        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModSounds.register(modEventBus);

        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);

        ModEnchantmentEffects.register(modEventBus);
        ModEntities.register(modEventBus);


        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {

        }

        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {

        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.LUNARWOLF.get(), LunarWolfRenderer::new);
            EntityRenderers.register(ModEntities.NOCTRIUM_TOMAHAWK.get(), NoctriumTomahawkProjectileRenderer::new);
            EntityRenderers.register(ModEntities.VOIDHOWLER.get(), VoidHowlerRenderer::new);
        }
    }
}
