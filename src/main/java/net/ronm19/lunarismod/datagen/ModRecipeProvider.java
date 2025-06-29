package net.ronm19.lunarismod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.block.ModBlocks;
import net.ronm19.lunarismod.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        List<ItemLike> NOCTRIUM_SMETABLES = List.of(ModItems.RAW_NOCTRIUM_GEM.get(),
                ModBlocks.NOCTRIUM_ORE.get(), ModBlocks.NOCTRIUM_DEEPSLATE_ORE.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.NOCTRIUM_BLOCK.get())
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', ModItems.NOCTRIUMGEM.get())
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.MOON_FRUIT_STEW.get())
                .pattern(" G ")
                .pattern(" S ")
                .pattern(" B ")
                .define('G', Items.GLOW_BERRIES)
                .define('S', Items.SWEET_BERRIES)
                .define('B', Items.BOWL)
                .unlockedBy(getHasName(Items.GLOW_BERRIES), has(Items.GLOW_BERRIES))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.NOCTRIUM_SWORD.get())
                .pattern(" N ")
                .pattern(" N ")
                .pattern(" S ")
                .define('N', ModItems.NOCTRIUMGEM.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.NOCTRIUM_PICKAXE.get())
                .pattern("NNN")
                .pattern(" S ")
                .pattern(" S ")
                .define('N', ModItems.NOCTRIUMGEM.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.NOCTRIUM_SHOVEL.get())
                .pattern(" N ")
                .pattern(" S ")
                .pattern(" S ")
                .define('N', ModItems.NOCTRIUMGEM.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.NOCTRIUM_AXE.get())
                .pattern("NN ")
                .pattern("NS ")
                .pattern(" S ")
                .define('N', ModItems.NOCTRIUMGEM.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.NOCTRIUM_HOE.get())
                .pattern("NN ")
                .pattern(" S ")
                .pattern(" S ")
                .define('N', ModItems.NOCTRIUMGEM.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.NOCTRIUM_HAMMER.get())
                .pattern("NNN")
                .pattern(" S ")
                .pattern(" S ")
                .define('N', ModBlocks.NOCTRIUM_BLOCK.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModBlocks.NOCTRIUM_BLOCK.get()), has(ModBlocks.NOCTRIUM_BLOCK.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.NOCTRIUM_HELMET.get())
                .pattern("NNN")
                .pattern("N N")
                .define('N', ModItems.NOCTRIUMGEM.get())
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.NOCTRIUM_CHESTPLATE.get())
                .pattern("N N")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', ModItems.NOCTRIUMGEM.get())
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.NOCTRIUM_LEGGINGS.get())
                .pattern("NNN")
                .pattern("N N")
                .pattern("N N")
                .define('N', ModItems.NOCTRIUMGEM.get())
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.NOCTRIUM_BOOTS.get())
                .pattern("N N")
                .pattern("N N")
                .define('N', ModItems.NOCTRIUMGEM.get())
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get()))
                .save(pRecipeOutput);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.NOCTRIUMGEM.get(), 9)
                .requires(ModBlocks.NOCTRIUM_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.NOCTRIUM_BLOCK.get()), has(ModBlocks.NOCTRIUM_BLOCK.get())).save(pRecipeOutput);

        oreSmelting(pRecipeOutput, NOCTRIUM_SMETABLES, RecipeCategory.MISC, ModItems.NOCTRIUMGEM.get(), 0.29f, 200, "noctrium");
        oreBlasting(pRecipeOutput, NOCTRIUM_SMETABLES, RecipeCategory.MISC, ModItems.NOCTRIUMGEM.get(), 0.29f, 100, "noctrium");

        stairBuilder(ModBlocks.NOCTRIUM_STAIRS.get(), Ingredient.of(ModItems.NOCTRIUMGEM.get())).group("noctrium")
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get())).save(pRecipeOutput);
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.NOCTRIUM_SLAB.get(), ModItems.NOCTRIUMGEM.get());

        buttonBuilder(ModBlocks.NOCTRIUM_BUTTON.get(), Ingredient.of(ModItems.NOCTRIUMGEM.get())).group("noctrium")
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get())).save(pRecipeOutput);
        pressurePlate(pRecipeOutput, ModBlocks.NOCTRIUM_PRESSURE_PLATE.get(), ModItems.NOCTRIUMGEM.get());

        fenceBuilder(ModBlocks.NOCTRIUM_FENCE.get(), Ingredient.of(ModItems.NOCTRIUMGEM.get())).group("noctrium")
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get())).save(pRecipeOutput);
        fenceGateBuilder(ModBlocks.NOCTRIUM_FENCE_GATE.get(), Ingredient.of(ModItems.NOCTRIUMGEM.get())).group("noctrium")
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get())).save(pRecipeOutput);
        wall(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.NOCTRIUM_WALL.get(), ModItems.NOCTRIUMGEM.get());

        doorBuilder(ModBlocks.NOCTRIUM_DOOR.get(), Ingredient.of(ModItems.NOCTRIUMGEM.get())).group("noctrium")
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get())).save(pRecipeOutput);
        trapdoorBuilder(ModBlocks.NOCTRIUM_TRAPDOOR.get(), Ingredient.of(ModItems.NOCTRIUMGEM.get())).group("noctrium")
                .unlockedBy(getHasName(ModItems.NOCTRIUMGEM.get()), has(ModItems.NOCTRIUMGEM.get())).save(pRecipeOutput);
    }



    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, LunarisMod.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
