package net.ronm19.lunarismod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
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
import net.ronm19.lunarismod.entity.client.*;
import net.ronm19.lunarismod.item.ModCreativeModeTabs;
import net.ronm19.lunarismod.item.ModItems;
import net.ronm19.lunarismod.sound.ModSounds;
import net.ronm19.lunarismod.potion.ModPotions;
import net.ronm19.lunarismod.worldgen.biome.ModBiomes;
import net.ronm19.lunarismod.worldgen.biome.ModSurfaceRules;
import org.slf4j.Logger;
import terrablender.api.SurfaceRuleManager;


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

        ModBiomes.registerBiomes();

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    private void commonSetup(final FMLCommonSetupEvent event) {


        // Register our surface rules
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.END, MOD_ID, ModSurfaceRules.makeVoidGroveRotRules());
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModSurfaceRules.makeVireClaveRules());
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

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
            EntityRenderers.register(ModEntities.LUNAR_SPEAR.get(), LunarSpearProjectileRenderer::new);
            EntityRenderers.register(ModEntities.VOIDHOWLER.get(), VoidHowlerRenderer::new);
            EntityRenderers.register(ModEntities.LUNARSENTINEL.get(), LunarSentinelRenderer::new);
            EntityRenderers.register(ModEntities.LUNAR_CREEPER.get(), LunarCreeperRenderer::new);
            EntityRenderers.register(ModEntities.VOID_PHANTOM.get(), VoidPhantomRenderer::new);
            EntityRenderers.register(ModEntities.VOID_ORB.get(), VoidOrbRenderer::new);
            EntityRenderers.register(ModEntities.VOID_EYE.get(), VoidEyeRenderer::new);
            EntityRenderers.register(ModEntities.LUNAR_ENDERMAN.get(), LunarEndermanRenderer::new);
            EntityRenderers.register(ModEntities.VELOMIR.get(), VelomirRenderer::new);
            EntityRenderers.register(ModEntities.LUNAR_ZOMBIE.get(), LunarZombieRenderer::new);
            EntityRenderers.register(ModEntities.LUNAR_HEROBRINE.get(), LunarHerobrineRenderer::new);
            EntityRenderers.register(ModEntities.LUNAR_ZOMBIE_KING.get(), LunarZombieKingRenderer::new);
            EntityRenderers.register(ModEntities.LUNAREON.get(), LunareonRenderer::new);
            EntityRenderers.register(ModEntities.LUNAR_KNIGHT.get(), LunarKnightRenderer::new);
            EntityRenderers.register(ModEntities.VOID_WARDEN.get(), VoidWardenRenderer::new);

        }
    }
}
