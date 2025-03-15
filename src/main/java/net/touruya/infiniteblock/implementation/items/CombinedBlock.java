package net.touruya.infiniteblock.implementation.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import io.github.thebusybiscuit.slimefun4.libraries.paperlib.PaperLib;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import net.touruya.infiniteblock.api.stored.SlimefunStored;
import net.touruya.infiniteblock.api.stored.Stored;
import net.touruya.infiniteblock.api.stored.VanillaStored;
import net.touruya.infiniteblock.utils.Constants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class CombinedBlock extends SlimefunItem {
    private static CombinedBlock instance;
    private static @Nullable String name;
    private static String[] lore;

    public CombinedBlock(@NotNull ItemGroup itemGroup, @NotNull SlimefunItemStack slimefunItemStack, @NotNull RecipeType recipeType, ItemStack @NotNull [] recipe) {
        super(itemGroup, slimefunItemStack, recipeType, recipe);
        instance = this;
        name = slimefunItemStack.getDisplayName();
        lore = slimefunItemStack.getItemMeta().getLore().toArray(new String[0]);
        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent blockPlaceEvent) {
            }
        });
    }

    public static @NotNull ItemStack createCombined(@NotNull Stored stored, long amount) {
        ItemStack clone = new CustomItemStack(stored.getItemStack().clone(), name, lore);
        writePDCToCombined(clone, stored, amount);
        updateLoreForCombined(clone);

        return clone;
    }

    public static void writePDCToCombined(@NotNull ItemStack itemStack, @NotNull Stored stored, long amount) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(Constants.STORED_KEY, PersistentDataType.STRING, stored.getIdentifier());
        meta.getPersistentDataContainer().set(Constants.STORED_AMOUNT_KEY, PersistentDataType.LONG, amount);
        Slimefun.getItemDataService().setItemData(meta, instance.getId());
        itemStack.setItemMeta(meta);
    }

    public static @Nullable Stored getStoredFromCombined(@NotNull ItemStack combined) {
        ItemMeta meta = combined.getItemMeta();
        String identifier = meta.getPersistentDataContainer().getOrDefault(Constants.STORED_KEY, PersistentDataType.STRING, "");
        Stored stored;
        stored = VanillaStored.loadFromIdentifier(identifier);
        if (stored == null) {
            stored = SlimefunStored.loadFromIdentifier(identifier);
        }

        return stored;
    }

    public static long getStoredAmountFromCombined(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return meta.getPersistentDataContainer().getOrDefault(Constants.STORED_AMOUNT_KEY, PersistentDataType.LONG, 0L);
    }

    public static void updateLoreForCombined(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            return;
        }

        if (lore.size() < 2) {
            return;
        }

        if (isInfinity(itemStack)) {
            lore.set(1, ChatColor.translateAlternateColorCodes('&', "&6已存储: " + getStoredFromCombined(itemStack).getName() + "x " + Constants.INFINITY_STRING));
        } else {
            lore.set(1, ChatColor.translateAlternateColorCodes('&', "&6已存储: " + getStoredFromCombined(itemStack).getName() + "x " + getStoredAmountFromCombined(itemStack)));
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    public static void updateActionBarForPlayer(@NotNull Player player, @NotNull Stored stored, long amount) {
        player.sendActionBar(Component.text("剩余: " + stored.getName() + "x " + (isInfinity(amount) ? Constants.INFINITY_STRING : amount)));
    }

    public static boolean isInfinity(@NotNull ItemStack itemStack) {
        return getStoredAmountFromCombined(itemStack) >= Constants.INFINITY_THRESHOLD;
    }

    public static boolean isInfinity(long amount) {
        return amount >= Constants.INFINITY_THRESHOLD;
    }

    public void use(@NotNull Player player, @NotNull ItemStack combined, @NotNull Location location) {
        Stored stored = getStoredFromCombined(combined);
        if (stored == null) {
            return;
        }

        ItemStack storedItem = stored.getItemStack();
        if (!storedItem.getType().isBlock()) {
            return;
        }

        placeBlock(player, combined, stored, location);
        updateLoreForCombined(combined);
    }

    public void placeBlock(@NotNull Player player, @NotNull ItemStack combined, @NotNull Stored stored, @NotNull Location location) {
        final long currentAmount = getStoredAmountFromCombined(combined);
        if (currentAmount <= 0) {
            player.sendMessage("融合方块已用完");
            return;
        }

        final Block block = location.getBlock();
        final Material material = stored.getItemStack().getType();
        if (stored instanceof SlimefunStored ss) {
            final SlimefunItem slimefunItem = ss.getItem();
            Slimefun.getDatabaseManager().getBlockDataController().createBlock(location, slimefunItem.getId());
            if (material == Material.PLAYER_HEAD || material == Material.PLAYER_WALL_HEAD) {
                if (slimefunItem.getItem() instanceof SlimefunItemStack sfis) {
                    String texture = sfis.getSkullTexture().orElse(null);
                    if (texture != null) {
                        PlayerSkin skin = PlayerSkin.fromBase64(texture);
                        PlayerHead.setSkin(block, skin, false);
                    }
                }
            }
        }

        long leftAmount = currentAmount;
        if (!isInfinity(combined)) {
            leftAmount -= 1;
            writePDCToCombined(combined, stored, leftAmount);
        }

        if (PaperLib.isPaper()) {
            updateActionBarForPlayer(player, stored, leftAmount);
        }
    }

    @Data
    @AllArgsConstructor
    public static class Entry {
        private final @NotNull Location location;
        private final @NotNull Stored stored;
    }
}
