package net.ronm19.lunarismod.worldgen.biome;

import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.worldgen.biome.region.OverworldRegion;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import terrablender.api.EndBiomeRegistry;
import terrablender.api.Regions;

public class ModBiomes {
    public static final ResourceKey<Biome> VOID_GROVE = registerBiomeKey("void_grove");
    public static final ResourceKey<Biome> VIRECLAVE = registerBiomeKey("vireclave");

    public static void registerBiomes() {
        Regions.register(new OverworldRegion(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "lunaris_overworld"), 30));

        EndBiomeRegistry.registerHighlandsBiome(VOID_GROVE, 60);
        EndBiomeRegistry.registerMidlandsBiome(VOID_GROVE, 60);
        EndBiomeRegistry.registerIslandBiome(VOID_GROVE, 60);
        EndBiomeRegistry.registerEdgeBiome(VOID_GROVE,60);
    }

    public static void bootstrap(BootstrapContext<Biome> context) {
        var carver = context.lookup(Registries.CONFIGURED_CARVER);
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        register(context, VOID_GROVE, ModEndBiomes.voidGrove(placedFeatures, carver));
        register(context, VIRECLAVE, ModOverworldBiomes.vireClave(placedFeatures, carver));
    }

    private static void register(BootstrapContext<Biome> context, ResourceKey<Biome> key, Biome biome) {
        context.register(key, biome);
    }

    private static ResourceKey<Biome> registerBiomeKey(String name) {
        return ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, name));
    }
}
