package net.ronm19.lunarismod.worldgen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.worldgen.biome.ModBiomes;

import java.util.List;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_NOCTRIUM_ORE = registerKey("add_noctrium_ore");
    public static final ResourceKey<BiomeModifier> ADD_NOCTRIUM_TREE = registerKey("add_noctrium_tree");

    public static final ResourceKey<BiomeModifier> SPAWN_LUNARWOLF = registerKey("spawn_lunarwolf");
    public static final ResourceKey<BiomeModifier> SPAWN_VOIDHOWLER = registerKey("spawn_voidhowler");
    public static final ResourceKey<BiomeModifier> SPAWN_LUNARSENTINEL = registerKey("spawn_lunarsentinel");
    public static final ResourceKey<BiomeModifier> SPAWN_LUNAR_CREEPER = registerKey("spawn_lunar_creeper");
    public static final ResourceKey<BiomeModifier> SPAWN_VOID_PHANTOM = registerKey("spawn_void_phantom");
    public static final ResourceKey<BiomeModifier> SPAWN_VOID_EYE = registerKey("spawn_void_eye");
    public static final ResourceKey<BiomeModifier> SPAWN_LUNAR_ENDERMAN = registerKey("spawn_lunar_enderman");
    public static final ResourceKey<BiomeModifier> SPAWN_VELOMIR = registerKey("spawn_velomir");
    public static final ResourceKey<BiomeModifier> SPAWN_LUNAR_ZOMBIE = registerKey("spawn_lunar_zombie");
    public static final ResourceKey<BiomeModifier> SPAWN_LUNAR_ZOMBIE_KING = registerKey("spawn_lunar_zombie_king");
    public static final ResourceKey<BiomeModifier> SPAWN_LUNAREON = registerKey("spawn_lunareon");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeature = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

    context.register(ADD_NOCTRIUM_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
           HolderSet.direct(biomes.getOrThrow(Biomes.DRIPSTONE_CAVES), biomes.getOrThrow(Biomes.LUSH_CAVES)),
            HolderSet.direct(placedFeature.getOrThrow(ModPlacedFeatures.NOCTRIUM_ORE_PLACED_KEY)),
            GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_NOCTRIUM_TREE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.PLAINS), biomes.getOrThrow(Biomes.SWAMP)),
                HolderSet.direct(placedFeature.getOrThrow(ModPlacedFeatures.NOCTRIUM_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));


        // ----------------------- Overworld Entities -------------------- //

        context.register(SPAWN_LUNARWOLF, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.FOREST), biomes.getOrThrow(Biomes.DARK_FOREST), biomes.getOrThrow(Biomes.BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_PINE_TAIGA), biomes.getOrThrow(Biomes.TAIGA), biomes.getOrThrow(Biomes.SNOWY_TAIGA), biomes.getOrThrow(Biomes.WINDSWEPT_FOREST), biomes.getOrThrow(Biomes.GROVE), biomes.getOrThrow(Biomes.PLAINS), biomes.getOrThrow(ModBiomes.VIRECLAVE)),
                List.of(new MobSpawnSettings.SpawnerData(ModEntities.LUNARWOLF.get(), 40, 10, 18))));
        context.register(SPAWN_VOIDHOWLER, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.FOREST), biomes.getOrThrow(Biomes.DARK_FOREST), biomes.getOrThrow(Biomes.BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_PINE_TAIGA), biomes.getOrThrow(Biomes.TAIGA), biomes.getOrThrow(Biomes.SNOWY_TAIGA), biomes.getOrThrow(Biomes.WINDSWEPT_FOREST), biomes.getOrThrow(Biomes.GROVE), biomes.getOrThrow(Biomes.PLAINS),biomes.getOrThrow(ModBiomes.VIRECLAVE)),
                List.of(new MobSpawnSettings.SpawnerData(ModEntities.VOIDHOWLER.get(), 10, 1, 2))));
        context.register(SPAWN_LUNARSENTINEL, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.FOREST), biomes.getOrThrow(Biomes.DARK_FOREST), biomes.getOrThrow(Biomes.BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_PINE_TAIGA), biomes.getOrThrow(Biomes.TAIGA), biomes.getOrThrow(Biomes.SNOWY_TAIGA), biomes.getOrThrow(Biomes.WINDSWEPT_FOREST), biomes.getOrThrow(Biomes.GROVE), biomes.getOrThrow(Biomes.PLAINS),biomes.getOrThrow(ModBiomes.VIRECLAVE)), List.of(new MobSpawnSettings.SpawnerData(ModEntities.LUNARSENTINEL.get(), 25, 3, 5))));
        context.register(SPAWN_LUNAR_CREEPER, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.FOREST), biomes.getOrThrow(Biomes.DARK_FOREST), biomes.getOrThrow(Biomes.BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_PINE_TAIGA), biomes.getOrThrow(Biomes.TAIGA), biomes.getOrThrow(Biomes.SNOWY_TAIGA), biomes.getOrThrow(Biomes.WINDSWEPT_FOREST), biomes.getOrThrow(Biomes.GROVE), biomes.getOrThrow(Biomes.PLAINS),biomes.getOrThrow(ModBiomes.VIRECLAVE)), List.of(new MobSpawnSettings.SpawnerData(ModEntities.LUNAR_CREEPER.get(), 40, 4, 6))));
        context.register(SPAWN_VELOMIR, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.FOREST), biomes.getOrThrow(Biomes.DARK_FOREST), biomes.getOrThrow(Biomes.BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_PINE_TAIGA), biomes.getOrThrow(Biomes.TAIGA), biomes.getOrThrow(Biomes.SNOWY_TAIGA), biomes.getOrThrow(Biomes.WINDSWEPT_FOREST), biomes.getOrThrow(Biomes.GROVE), biomes.getOrThrow(Biomes.PLAINS),biomes.getOrThrow(ModBiomes.VIRECLAVE)),
                List.of(new MobSpawnSettings.SpawnerData(ModEntities.VELOMIR.get(), 20, 3, 6))));
        context.register(SPAWN_LUNAR_ZOMBIE, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.FOREST), biomes.getOrThrow(Biomes.DARK_FOREST), biomes.getOrThrow(Biomes.BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_PINE_TAIGA), biomes.getOrThrow(Biomes.TAIGA), biomes.getOrThrow(Biomes.SNOWY_TAIGA), biomes.getOrThrow(Biomes.WINDSWEPT_FOREST), biomes.getOrThrow(Biomes.GROVE), biomes.getOrThrow(Biomes.PLAINS),biomes.getOrThrow(ModBiomes.VIRECLAVE)),
                List.of(new MobSpawnSettings.SpawnerData(ModEntities.LUNAR_ZOMBIE.get(), 20, 4, 8))));
        context.register(SPAWN_LUNAR_ZOMBIE_KING, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(ModBiomes.VIRECLAVE)), List.of(new MobSpawnSettings.SpawnerData(ModEntities.LUNAR_ZOMBIE_KING.get(), 10, 1, 1))));
        context.register(SPAWN_LUNAREON, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.FOREST), biomes.getOrThrow(Biomes.DARK_FOREST), biomes.getOrThrow(Biomes.BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_BIRCH_FOREST), biomes.getOrThrow(Biomes.OLD_GROWTH_PINE_TAIGA), biomes.getOrThrow(Biomes.TAIGA), biomes.getOrThrow(Biomes.SNOWY_TAIGA), biomes.getOrThrow(Biomes.WINDSWEPT_FOREST), biomes.getOrThrow(Biomes.GROVE), biomes.getOrThrow(Biomes.PLAINS), biomes.getOrThrow(ModBiomes.VIRECLAVE)),
                List.of(new MobSpawnSettings.SpawnerData(ModEntities.LUNAREON.get(), 25, 5, 7))));


        //---------------------- End Entities ----------------- //

        context.register(SPAWN_VOID_PHANTOM, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(ModBiomes.VOID_GROVE)), List.of(new MobSpawnSettings.SpawnerData(ModEntities.VOID_PHANTOM.get(), 40, 4, 6))));
        context.register(SPAWN_VOID_EYE, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(ModBiomes.VIRECLAVE)), List.of(new MobSpawnSettings.SpawnerData(ModEntities.VOID_EYE.get(), 40, 3, 4))));
        context.register(SPAWN_LUNAR_ENDERMAN, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(ModBiomes.VOID_GROVE)), List.of(new MobSpawnSettings.SpawnerData(ModEntities.LUNAR_ENDERMAN.get(), 40, 5, 9))));


    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, name));
    }
}
