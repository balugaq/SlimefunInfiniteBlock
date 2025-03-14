package net.touruya.infiniteblock.implementation.items;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import net.touruya.infiniteblock.api.stored.Stored;
import net.touruya.infiniteblock.core.commands.StorageCommand;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class Combiner extends SlimefunItem {
    private static final int[] BACKGROUND_SLOTS = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 10, 11, 12, 13, 14, 15, 16, 17,
            18, 20, 21, 22, 23, 24, 26,
            27, 28, 29, 30, 32, 33, 34, 35,
            36, 37, 38, 39, 41, 42, 43, 44,
            45, 46, 47, 48, 49, 50, 51, 52, 53,
    };
    private static final ItemStack BACKGROUND = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " ", " ");
    private static final int[] INPUT_SLOTS = {19, 25};
    private static final int CRAFT_SLOT = 31;
    private static final int OUTPUT_SLOT = 40;
    private static final ItemStack CRAFT_ICON = new CustomItemStack(Material.DIAMOND, "Combiner", "Craft");

    public Combiner(@NotNull ItemGroup category, @NotNull SlimefunItemStack item, @NotNull RecipeType recipeType, ItemStack @NotNull [] recipe) {
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
                menu.addMenuClickHandler(CRAFT_SLOT, (p, s, i1, a) -> {
                    craft(p, menu);
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
                    return INPUT_SLOTS;
                } else {
                    return new int[]{OUTPUT_SLOT};
                }
            }
        };
    }

    public static boolean isCombinedBlock(ItemStack itemStack) {
        return SlimefunItem.getByItem(itemStack) instanceof CombinedBlock;
    }

    public static @NotNull ItemStack getUnpackedItem(@NotNull ItemStack combined) {
        Stored stored = CombinedBlock.getStoredFromCombined(combined);
        if (stored == null) {
            return new ItemStack(Material.AIR);
        }
        long amount = CombinedBlock.getStoredAmountFromCombined(combined);
        return new CustomItemStack(stored.getItemStack(), (int) amount);
    }

    @CanIgnoreReturnValue
    public static boolean craft(@NotNull Player player, @NotNull BlockMenu menu) {
        ItemStack innerItem = null;
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

            if (innerItem == null) {
                if (!isCombinedBlock(itemStack)){
                    innerItem = itemStack;
                } else {
                    innerItem = getUnpackedItem(itemStack);
                }
            } else {
                if (!SlimefunUtils.isItemSimilar(innerItem, itemStack, true, false)) {
                    isAllSimilar = false;
                    break;
                }
            }

            totalAmount += itemStack.getAmount();
        }

        if (innerItem == null) {
            player.sendMessage("请确保至少有一个输入物品");
            return false;
        }

        if (!isAllSimilar) {
            player.sendMessage("请确保所有输入物品都相同");
            return false;
        }

        if (innerItem.getType() == Material.AIR || !innerItem.getType().isBlock()) {
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
        ItemStack itemStack = StorageCommand.create(innerItem, totalAmount);

        menu.pushItem(itemStack, OUTPUT_SLOT);
        player.sendMessage("合成成功");

        return true;
    }
}
