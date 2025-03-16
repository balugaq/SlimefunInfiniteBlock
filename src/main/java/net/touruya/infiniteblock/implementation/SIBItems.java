package net.touruya.infiniteblock.implementation;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import lombok.experimental.UtilityClass;
import net.touruya.infiniteblock.implementation.items.CombinedBlock;
import net.touruya.infiniteblock.implementation.items.Combiner;
import net.touruya.infiniteblock.implementation.items.InfiniteStar;
import net.touruya.infiniteblock.implementation.items.ItemFountainBalancer;
import net.touruya.infiniteblock.implementation.items.StarOperator;
import net.touruya.infiniteblock.utils.Icons;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@UtilityClass
public class SIBItems {
    public static Combiner COMBINER;
    public static CombinedBlock COMBINED_BLOCK;
    public static InfiniteStar INFINITE_STAR;
    public static StarOperator STAR_OPERATOR;
    public static ItemFountainBalancer ITEM_FOUNTAIN_BALANCER;

    public static void setup(@NotNull InfiniteBlocks plugin) {
        //方块融合
        COMBINER = new Combiner(
                SIBGroups.MACHINES_GROUP,
                Icons.COMBINER,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        SlimefunItems.WITHER_PROOF_GLASS, SlimefunItem.getById("ANDROID_MEMORY_CORE").getItem(), SlimefunItems.WITHER_PROOF_GLASS,
                        SlimefunItem.getById("COOLING_UNIT").getItem(), SlimefunItem.getById("NTW_QUANTUM_STORAGE_8").getItem(), SlimefunItem.getById("COOLING_UNIT").getItem(),
                        SlimefunItems.WITHER_PROOF_GLASS, SlimefunItem.getById("ANDROID_MEMORY_CORE").getItem(),SlimefunItems.WITHER_PROOF_GLASS
                }
        );//我好像知道了 我是纸张

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
//方块操作
        STAR_OPERATOR = new StarOperator(
                SIBGroups.MACHINES_GROUP,
                Icons.STAR_OPERATOR,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        SlimefunItem.getById("WITHER_PROOF_GLASS").getItem(), SlimefunItem.getById("NTW_RADIOACTIVE_OPTIC_STAR").getItem(), SlimefunItem.getById("WITHER_PROOF_GLASS").getItem(),
                        SlimefunItem.getById("NTW_ADVANCED_NANOBOTS").getItem(), Icons.COMBINER, SlimefunItem.getById("NTW_ADVANCED_NANOBOTS").getItem(),
                        SlimefunItem.getById("WITHER_PROOF_GLASS").getItem(), SlimefunItem.getById("NTW_ADVANCED_NANOBOTS").getItem(), SlimefunItem.getById("WITHER_PROOF_GLASS").getItem()
                }
        );
//物品爆发
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
        STAR_OPERATOR.register(plugin);
        ITEM_FOUNTAIN_BALANCER.register(plugin);
    }
}
