package net.touruya.infiniteblock.implementation.items;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import net.touruya.infiniteblock.api.stored.Stored;
import net.touruya.infiniteblock.utils.BlockMenuUtil;
import net.touruya.infiniteblock.utils.Constants;
import net.touruya.infiniteblock.utils.Icons;
import net.touruya.infiniteblock.utils.StoredUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class ENItemFountainBalancer extends AContainer implements RecipeDisplayItem {
    public static final int[] BACKGROUND_SLOTS = {
            0, 1, 2, 3, 5, 6, 7, 8,
            45, 46, 47, 48, 50, 51, 52, 53,
    };
    public static final int PROGRESS_SLOT = 49;
    public static final int[] OUTPUT_SLOTS = {
            9, 10, 11, 12, 13, 14, 15, 16, 17,
            18, 19, 20, 21, 22, 23, 24, 25, 26,
            27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 40, 41, 42, 43, 44,
    };
    public static final int COMBINED_SLOT = 4;

    public ENItemFountainBalancer(@NotNull ItemGroup category, @NotNull SlimefunItemStack item, @NotNull RecipeType recipeType, ItemStack @NotNull [] recipe) {
        super(category, item, recipeType, recipe);
        new BlockMenuPreset(this.getId(), getItemName()) {

            @Override
            public void init() {
                for (int i : BACKGROUND_SLOTS) {
                    addItem(i, Icons.BACKGROUND, ChestMenuUtils.getEmptyClickHandler());
                }
                addItem(PROGRESS_SLOT, Icons.BACKGROUND, ChestMenuUtils.getEmptyClickHandler());
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return player.hasPermission("slimefun.inventory.bypass") || Slimefun.getProtectionManager().hasPermission(player, block, Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                if (itemTransportFlow == ItemTransportFlow.INSERT) {
                    return OUTPUT_SLOTS;
                } else {
                    return new int[0];
                }
            }
        };
    }

    public static void feedback(@NotNull BlockMenu menu, @NotNull String message, boolean success) {
        if (menu.hasViewer()) {
            menu.replaceExistingItem(PROGRESS_SLOT, new CustomItemStack(
                    success ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE,
                    " ",
                    (success ? "&a" : "&c") + message
            ));
        }
    }

    @Override
    public void tick(@NotNull Block block) {
        BlockMenu menu = StorageCacheUtils.getMenu(block.getLocation());
        if (menu == null) {
            return;
        }

        craft(menu);
    }

    @Override
    public @NotNull ItemStack getProgressBar() {
        return Icons.BACKGROUND;
    }

    @Override
    public @NotNull String getMachineIdentifier() {
        return getId();
    }

    @CanIgnoreReturnValue
    public boolean craft(@NotNull BlockMenu menu) {
        if (!this.takeCharge(menu.getLocation())) {
            feedback(menu, "电力不足", false);
            return false;
        }

        ItemStack combined = menu.getItemInSlot(COMBINED_SLOT);
        if (combined == null || combined.getType() == Material.AIR) {
            feedback(menu, "没有放入无尽方块", false);
            return false;
        }

        Stored combinedStored = StoredUtils.getStoredFromCombined(combined);
        if (combinedStored == null) {
            feedback(menu, "无尽方块已损坏", false);
            return false;
        }

        ItemStack innerItem = StoredUtils.getUnpackedItemFromCombined(combined);
        if (innerItem.getType() == Material.AIR) {
            feedback(menu, "无尽方块内没有物品", false);
            return false;
        }

        int beforeAmount = innerItem.getAmount();
        ItemStack left = BlockMenuUtil.pushItem(menu, innerItem, OUTPUT_SLOTS);
        int afterAmount = left == null ? 0 : left.getAmount();
        if (beforeAmount == afterAmount) {
            feedback(menu, "输出槽已满", false);
            return false;
        }

        if (!StoredUtils.isInfinity(combined)) {
            CombinedBlock.writePDCToCombined(combined, combinedStored, afterAmount);
            if (menu.hasViewer()) {
                StoredUtils.updateLoreForCombined(combined);
            }
        }

        feedback(menu, "工作中", true);

        return true;
    }

    @Override
    public int getCapacity() {
        return 8192;
    }

    @Override
    public int getEnergyConsumption() {
        return 128;
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    @Override
    @NotNull
    public List<ItemStack> getDisplayRecipes() {

        return Constants.DESCRIPTION_ENITEM_FOUNTAIN_BALANCER;
    }
}
