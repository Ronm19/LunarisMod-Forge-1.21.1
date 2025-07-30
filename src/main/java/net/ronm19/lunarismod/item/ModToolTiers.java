package net.ronm19.lunarismod.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.ronm19.lunarismod.util.ModTags;

public class ModToolTiers {
    public static Tier NOCTRIUM = new ForgeTier(2500, 8.5f, 4f, 18,
            ModTags.Blocks.NEEDS_NOCTRIUM_TOOL, () -> Ingredient.of(ModItems.NOCTRIUMGEM.get()),
            ModTags.Blocks.INCORRECT_FOR_NOCTRIUM_TOOL);

    public static Tier LUNAR_HEROBRINE = new ForgeTier(5000, 8.5f, 7f, 18,
           ModTags.Blocks.NEEDS_LUNAR_HEROBRINE_T00L, () -> Ingredient.of(ModItems.LUNAR_HEROBRINE_GEM.get()),
            ModTags.Blocks.INCORRECT_FOR_LUNAR_HEROBRINE_TOOL);
}
