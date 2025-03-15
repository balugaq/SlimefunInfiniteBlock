package net.touruya.infiniteblock.implementation;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import net.touruya.infiniteblock.utils.Constants;
import net.touruya.infiniteblock.utils.Icons;

@UtilityClass
public class SIBRecipeTypes {
    public static RecipeType RECIPE_TYPE_COMBINER;
    public static RecipeType RECIPE_TYPE_BREAK_BLOCK;

    public static void setup(InfiniteBlocks plugin) {
        SIBRecipeTypes.RECIPE_TYPE_COMBINER = new RecipeType(
                Constants.COMBINER,
                Icons.COMBINER
        );
        ;

        SIBRecipeTypes.RECIPE_TYPE_BREAK_BLOCK = new RecipeType(
                Constants.BREAK_BLOCK,
                Icons.BREAK_BLOCK
        );
    }
}
