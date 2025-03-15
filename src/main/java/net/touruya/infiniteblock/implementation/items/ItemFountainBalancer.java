package net.touruya.infiniteblock.implementation.items;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import net.touruya.infiniteblock.api.stored.Stored;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ItemFountainBalancer extends AContainer {
    public static final ItemStack BACKGROUND = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " ", " ");
    public static final int[] BACKGROUND_SLOTS = {
            0, 2, 3, 5, 6, 8,
            45, 46, 47, 48, 50, 51, 52, 53,
    };
    public static final int PROGRESS_SLOT = 4;
    public static final int[] OUTPUT_SLOTS = {
            9, 10, 11, 12, 13, 14, 15, 16, 17,
            18, 19, 20, 21, 22, 23, 24, 25, 26,
            27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 40, 41, 42, 43, 44,
    };
    public static final int COMBINED_SLOT = 1;
    public static final int STAR_SLOT = 7;

    public ItemFountainBalancer(@NotNull ItemGroup category, @NotNull SlimefunItemStack item, @NotNull RecipeType recipeType, ItemStack @NotNull [] recipe) {
        super(category, item, recipeType, recipe);
        new BlockMenuPreset(this.getId(), getItemName()) {

            @Override
            public void init() {
                for (int i : BACKGROUND_SLOTS) {
                    addItem(i, BACKGROUND, ChestMenuUtils.getEmptyClickHandler());
                }
                addItem(PROGRESS_SLOT, BACKGROUND, ChestMenuUtils.getEmptyClickHandler());
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

    public static boolean isCombinedBlock(ItemStack itemStack) {
        return SlimefunItem.getByItem(itemStack) instanceof CombinedBlock;
    }

    public static @NotNull ItemStack getUnpackedItemFromCombined(@NotNull ItemStack combined) {
        Stored stored = CombinedBlock.getStoredFromCombined(combined);
        if (stored == null) {
            return new ItemStack(Material.AIR);
        }
        long amount = CombinedBlock.getStoredAmountFromCombined(combined);
        if (amount <= 0) {
            return new ItemStack(Material.AIR);
        }
        return new CustomItemStack(stored.getItemStack(), (int) amount);
    }

    public static boolean isStar(ItemStack itemStack) {
        return SlimefunItem.getByItem(itemStack) instanceof InfiniteStar;
    }

    public static @NotNull ItemStack getUnpackedItemFromStar(@NotNull ItemStack star) {
        Stored stored = InfiniteStar.getStoredFromStar(star);
        if (stored == null) {
            return new ItemStack(Material.AIR);
        }
        return new CustomItemStack(stored.getItemStack(), 1);
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
        return BACKGROUND;
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
            feedback(menu, "没有放入融合方块", false);
            return false;
        }

        Stored combinedStored = CombinedBlock.getStoredFromCombined(combined);
        if (combinedStored == null) {
            feedback(menu, "融合方块已损坏", false);
            return false;
        }

        ItemStack innerItem = getUnpackedItemFromCombined(combined);
        if (innerItem.getType() == Material.AIR) {
            feedback(menu, "融合方块内没有物品", false);
            return false;
        }

        int beforeAmount = innerItem.getAmount();
        menu.pushItem(innerItem, OUTPUT_SLOTS);
        int afterAmount = innerItem.getAmount();

        boolean hasStar = false;
        ItemStack star = menu.getItemInSlot(STAR_SLOT); // optional
        if (star != null && star.getType() != Material.AIR) {
            Stored starStored = InfiniteStar.getStoredFromStar(star);
            if (starStored != null) {
                if (Objects.equals(combinedStored.getIdentifier(), starStored.getIdentifier())) {
                    hasStar = true;
                }
            }
        }

        if (beforeAmount != afterAmount && !CombinedBlock.isInfinity(combined)) {
            CombinedBlock.writePDCToCombined(combined, combinedStored, hasStar ? afterAmount : beforeAmount - 1);
            if (menu.hasViewer()) {
                CombinedBlock.updateLoreForCombined(combined);
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
}
