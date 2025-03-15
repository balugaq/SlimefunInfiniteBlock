package net.touruya.infiniteblock.implementation;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import lombok.experimental.UtilityClass;
import net.touruya.infiniteblock.implementation.items.CombinedBlock;
import net.touruya.infiniteblock.implementation.items.Combiner;
import net.touruya.infiniteblock.implementation.items.InfiniteStar;
import net.touruya.infiniteblock.implementation.items.ItemFountainBalancer;
import net.touruya.infiniteblock.utils.Icons;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class SIBItems {
    public static Combiner COMBINER;
    public static CombinedBlock COMBINED_BLOCK;
    public static InfiniteStar INFINITE_STAR;
    public static ItemFountainBalancer ITEM_FOUNTAIN_BALANCER;

    public static void setup(@NotNull InfiniteBlocks plugin) {
        COMBINER = new Combiner(
                SIBGroups.MACHINES_GROUP,
                Icons.COMBINER,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR,
                        SlimefunItems.CARBONADO, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.CARBONADO,
                        SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR
                }
        );

        COMBINED_BLOCK = new CombinedBlock(
                SIBGroups.MATERIALS_GROUP,
                Icons.COMBINED_BLOCK,
                SIBRecipeTypes.RECIPE_TYPE_COMBINER,
                new ItemStack[0]
        );

        INFINITE_STAR = new InfiniteStar(
                SIBGroups.MATERIALS_GROUP,
                Icons.INFINITE_STAR,
                SIBRecipeTypes.RECIPE_TYPE_BREAK_BLOCK,
                new ItemStack[0]
        );

        ITEM_FOUNTAIN_BALANCER = new ItemFountainBalancer(
                SIBGroups.MACHINES_GROUP,
                Icons.ITEM_FOUNTAIN_BALANCER,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR,
                        SlimefunItems.CARBONADO, Icons.COMBINER, SlimefunItems.CARBONADO,
                        SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR
                }
        );

        COMBINER.register(plugin);
        COMBINED_BLOCK.register(plugin);
        INFINITE_STAR.register(plugin);
        ITEM_FOUNTAIN_BALANCER.register(plugin);
    }
}
