package net.ronm19.lunarismod.worldgen.biome;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.ronm19.lunarismod.block.ModBlocks;

public class ModSurfaceRules {
    private static final SurfaceRules.RuleSource END_STONE = makeStateRule(Blocks.END_STONE);
    private static final SurfaceRules.RuleSource LUNAR_GRASS_BLOCK = makeStateRule(ModBlocks.LUNAR_GRASS_BLOCK.get());

    private static final SurfaceRules.RuleSource HUSK_STONE_BLOCK = makeStateRule(ModBlocks.HUSK_STONE_BLOCK.get());
    private static final SurfaceRules.RuleSource MOONSTONE_BLOCK = makeStateRule(ModBlocks.MOONSTONE_BLOCK.get());

    public static SurfaceRules.RuleSource makeVireClaveRules() {
        return SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.VIRECLAVE),
                SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, LUNAR_GRASS_BLOCK),
                        SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, HUSK_STONE_BLOCK),
                        SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, MOONSTONE_BLOCK)
                )
        );
    }

    public static SurfaceRules.RuleSource makeVoidGroveRotRules() {
        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.VOID_GROVE), LUNAR_GRASS_BLOCK),

                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, END_STONE)

        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
