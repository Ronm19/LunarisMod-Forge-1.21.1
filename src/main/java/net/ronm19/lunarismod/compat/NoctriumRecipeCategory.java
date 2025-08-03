package net.ronm19.lunarismod.compat;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class NoctriumRecipeCategory implements IRecipeCategory<NoctriumRecipeCategory> {
    @Override
    public RecipeType<NoctriumRecipeCategory> getRecipeType() {
        return null;
    }

    @Override
    public Component getTitle() {
        return null;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return null;
    }

    @Override
    public void setRecipe( IRecipeLayoutBuilder iRecipeLayoutBuilder, NoctriumRecipeCategory noctriumRecipeCategory, IFocusGroup iFocusGroup ) {

    }
}
