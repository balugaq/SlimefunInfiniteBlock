package net.touruya.infiniteblock.implementation.items;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import net.touruya.infiniteblock.api.objects.CustomSlimefunItem;
import net.touruya.infiniteblock.utils.Constants;
import net.touruya.infiniteblock.utils.Icons;
import net.touruya.infiniteblock.utils.StackUtils;
import net.touruya.infiniteblock.utils.StoredUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class StarOperator extends CustomSlimefunItem implements RecipeDisplayItem {
    public static final int[] BACKGROUND_SLOTS = {
            3, 4, 5, 12, 14, 21, 22, 23,
            27, 28, 29, 33, 34, 35,
            36, 37, 38, 42, 43, 44,
            45, 46, 47, 51, 52, 53
    };

    public static final int CRAFT_SLOT = 13;
    public static final int OUTPUT_SLOT = 40;
    public static final int COMBINED_SLOT = 10;
    public static final int STAR_SLOT = 16;
    public static final int[] INPUT_BORDER_SLOTS = {
            0, 1, 2, 6, 7, 8, 9, 11, 15, 17,
            18, 19, 20, 24, 25, 26
    };

    public static final int[] OUTPUT_BORDER_SLOTS = {
            30, 31, 32, 39, 41, 48, 49, 50
    };

    public StarOperator(@NotNull ItemGroup category, @NotNull SlimefunItemStack item, @NotNull RecipeType recipeType, ItemStack @NotNull [] recipe) {
        super(category, item, recipeType, recipe);
        new BlockMenuPreset(this.getId(), getItemName()) {

            @Override
            public void init() {
                for (int i : BACKGROUND_SLOTS) {
                    addItem(i, Icons.BACKGROUND, ChestMenuUtils.getEmptyClickHandler());
                }
                for (int i : INPUT_BORDER_SLOTS) {
                    addItem(i, Icons.INPUT_BORDER, ChestMenuUtils.getEmptyClickHandler());
                }
                for (int i : OUTPUT_BORDER_SLOTS) {
                    addItem(i, Icons.OUTPUT_BORDER, ChestMenuUtils.getEmptyClickHandler());
                }
                addItem(CRAFT_SLOT, Icons.CRAFT, ChestMenuUtils.getEmptyClickHandler());
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                menu.addMenuClickHandler(CRAFT_SLOT, (player, slot, clickType, item) -> {
                    craft(menu);
                    return false;
                });
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return player.hasPermission("slimefun.inventory.bypass") || Slimefun.getProtectionManager().hasPermission(player, block, Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                if (itemTransportFlow == ItemTransportFlow.INSERT) {
                    return new int[]{COMBINED_SLOT, STAR_SLOT};
                } else {
                    return new int[]{OUTPUT_SLOT};
                }
            }
        };
    }

    public static void feedback(@NotNull BlockMenu menu, @NotNull String message, boolean success) {
        if (menu.hasViewer()) {
            menu.replaceExistingItem(CRAFT_SLOT, new CustomItemStack(
                    success ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE,
                    " ",
                    (success ? "&a" : "&c") + message
            ));
        }
    }

    @CanIgnoreReturnValue
    public boolean craft(@NotNull BlockMenu menu) {
        ItemStack combined = menu.getItemInSlot(COMBINED_SLOT);
        if (combined == null || combined.getType() == Material.AIR) {
            feedback(menu, "未放入无尽方块", false);
            return false;
        }

        ItemStack star = menu.getItemInSlot(STAR_SLOT);
        if (star == null || star.getType() == Material.AIR) {
            feedback(menu, "未放入无限之星", false);
            return false;
        }

        ItemStack innerItemStack = StoredUtils.getUnpackedItemFromCombined(combined);
        if (innerItemStack.getType() == Material.AIR) {
            feedback(menu, "无尽方块无效", false);
            return false;
        }

        if (innerItemStack.getAmount() < Constants.INFINITY_THRESHOLD) {
            feedback(menu, "无尽方块数量不足", false);
            return false;
        }

        ItemStack starItemStack = StoredUtils.getUnpackedItemFromStar(star);
        if (starItemStack.getType() == Material.AIR) {
            feedback(menu, "无限之星无效", false);
            return false;
        }

        if (!StackUtils.itemsMatch(innerItemStack, starItemStack)) {
            feedback(menu, "操作的物品不一致", false);
            return false;
        }

        menu.consumeItem(COMBINED_SLOT, 1);
        menu.consumeItem(STAR_SLOT, 1);

        ItemStack itemStack = StoredUtils.createCombined(innerItemStack, Long.MAX_VALUE);
        menu.pushItem(itemStack, OUTPUT_SLOT);
        feedback(menu, "成功", true);

        return true;
    }

    @Override
    @NotNull
    public List<ItemStack> getDisplayRecipes() {
        return Constants.DESCRIPTION_STAR_OPERATOR;
    }
}
