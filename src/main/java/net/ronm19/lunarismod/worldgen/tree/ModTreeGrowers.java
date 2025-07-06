package net.ronm19.lunarismod.worldgen.tree;

import net.minecraft.world.level.block.grower.TreeGrower;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.worldgen.ModConfiguredFeatures;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower NOCTRIUM = new TreeGrower(LunarisMod.MOD_ID + ":noctrium",
            Optional.empty(), Optional.of(ModConfiguredFeatures.NOCTRIUM_KEY), Optional.empty());
}
