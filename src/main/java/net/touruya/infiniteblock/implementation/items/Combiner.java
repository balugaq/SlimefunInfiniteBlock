package net.touruya.infiniteblock.implementation.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import net.touruya.infiniteblock.api.stored.Stored;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class Combiner extends SlimefunItem {
    private static final int[] BACKGROUND_SLOTS = {
            0,  1,  2,  3,  4,  5,  6,  7,  8,
            9,  10, 11, 12, 13, 14, 15, 16, 17,
            18,     20, 21, 22, 23, 24,     26,
            27, 28, 29, 30,     32, 33, 34, 35,
            36, 37, 38, 39,     41, 42, 43, 44,
            45, 46, 47, 48, 49, 50, 51, 52, 53,
    };
    private static final ItemStack BACKGROUND = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " ", " ");
    private static final int[] INPUT_SLOTS = {19, 25};
    private static final int CRAFT_SLOT = 31;
    private static final int OUTPUT_SLOT = 40;
    private static final ItemStack CRAFT_ICON = new CustomItemStack(Material.DIAMOND, "Combiner", "Craft");

    public Combiner(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        new BlockMenuPreset(this.getId(), getItemName()) {

            @Override
            public void init() {
                for (int i : BACKGROUND_SLOTS) {
                    addItem(i, BACKGROUND, ChestMenuUtils.getEmptyClickHandler());
                }
                addItem(CRAFT_SLOT, CRAFT_ICON);
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                addItem(CRAFT_SLOT, CRAFT_ICON, (p, s, i1, a) -> {
                    craft(p, menu);
                    return false;
                });
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return false;
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                return new int[0];
            }
        };
    }

    public static boolean isCombinedBlock(ItemStack itemStack) {
        return SlimefunItem.getByItem(itemStack) instanceof CombinedBlock;
    }

    public static ItemStack getUnpackedItem(ItemStack itemStack) {
        Stored stored = CombinedBlock.getStored(itemStack);
        long amount = CombinedBlock.getStoredAmount(itemStack);
        return new CustomItemStack(stored.getItemStack(), (int) amount);
    }

    public static boolean craft(Player player, BlockMenu menu) {
        ItemStack template = null;
        long totalAmount = 0;
        boolean isAllSimilar = true;
        for (int inputSlot : INPUT_SLOTS) {
            ItemStack itemStack = menu.getItemInSlot(inputSlot);
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                continue;
            }

            if (isCombinedBlock(itemStack)) {
                itemStack = getUnpackedItem(itemStack);
            }

            if (template == null) {
                template = itemStack;
            } else {
                if (!SlimefunUtils.isItemSimilar(template, itemStack, true, false)) {
                    isAllSimilar = false;
                    break;
                }
            }

            totalAmount += itemStack.getAmount();
        }

        if (!isAllSimilar) {
            player.sendMessage("请确保所有输入物品都相同");
            return false;
        }

        if (template.getType() == Material.AIR || !template.getType().isBlock()) {
            player.sendMessage("请确保输入物品为方块");
            return false;
        }

        if (totalAmount <= 0) {
            player.sendMessage("请确保输入物品数量大于0");
            return false;
        }

        ItemStack exisitingOutput = menu.getItemInSlot(OUTPUT_SLOT);
        if (exisitingOutput != null && exisitingOutput.getType() != Material.AIR) {
            player.sendMessage("输出槽已有物品");
            return false;
        }

        // consume items
        for (int inputSlot : INPUT_SLOTS) {
            menu.replaceExistingItem(inputSlot, new ItemStack(Material.AIR));
        }

        if (totalAmount > Integer.MAX_VALUE) {
            player.sendMessage("输入物品数量过多");
            return false;
        }

        // push item
        ItemStack itemStack = CombinedBlock.createItemStack(
                CombinedBlock.getStored(template),
                totalAmount
        );

        menu.pushItem(itemStack, OUTPUT_SLOT);
        player.sendMessage("合成成功");

        return true;
    }
}
