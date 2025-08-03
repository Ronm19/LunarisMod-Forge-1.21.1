package net.ronm19.lunarismod.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.ronm19.lunarismod.LunarisMod;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_NOCTRIUM_TOOL = createTag("needs_noctrium_tool");
        public static final TagKey<Block> INCORRECT_FOR_NOCTRIUM_TOOL = createTag("incorrect_for_noctrium_tool");

        public static final TagKey<Block> NEEDS_LUNAR_HEROBRINE_T00L = createTag("needs_lunar_herobrine_tool");
        public static final TagKey<Block> INCORRECT_FOR_LUNAR_HEROBRINE_TOOL = createTag("incorrect_for_lunar_herobrine_tool");

        public static final TagKey<Block> TREE_BLOCKS = createTag("tree_blocks");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, name));
        }
    }

    public static class Items {

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, name));
        }
    }
}
