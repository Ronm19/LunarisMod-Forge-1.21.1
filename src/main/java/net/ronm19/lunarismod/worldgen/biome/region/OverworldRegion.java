package net.ronm19.lunarismod.worldgen.biome.region;

import com.mojang.datafixers.util.Pair;
import net.ronm19.lunarismod.worldgen.biome.ModBiomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

import static terrablender.api.ParameterUtils.*;

public class OverworldRegion extends Region {

    public OverworldRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();

        // Define surface-level Vireclave biome parameters
        new ParameterPointListBuilder()
                .temperature(Temperature.span(Temperature.ICY, Temperature.ICY)) // Cold but above freezing
                .humidity(Humidity.span(Humidity.ARID, Humidity.ARID))             // Dry and desolate
                .continentalness(Continentalness.span(Continentalness.INLAND, Continentalness.FAR_INLAND)) // Remote plateau
                .erosion(Erosion.span(Erosion.EROSION_2, Erosion.EROSION_3))       // Wider formations
                .depth(Depth.span(Depth.SURFACE, Depth.SURFACE))                  // Surface-based biome
                .weirdness(Weirdness.span(Weirdness.PEAK_VARIANT, Weirdness.PEAK_VARIANT)) // Elevated, alien feel
                .build().forEach(point -> builder.add(point, ModBiomes.VIRECLAVE));

        builder.build().forEach(mapper);
    }
}