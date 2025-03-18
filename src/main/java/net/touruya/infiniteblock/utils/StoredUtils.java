package net.touruya.infiniteblock.utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import net.touruya.infiniteblock.api.stored.SlimefunStored;
import net.touruya.infiniteblock.api.stored.Stored;
import net.touruya.infiniteblock.api.stored.VanillaStored;
import net.touruya.infiniteblock.implementation.items.CombinedBlock;
import net.touruya.infiniteblock.implementation.items.InfiniteStar;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StoredUtils {
    @Nullable
    public static ItemStack createCombined(@NotNull ItemStack holdingItem, long amount) {
        Stored stored = getStored(holdingItem);
        if (stored == null) {
            return null;
        }
        return CombinedBlock.createCombined(stored, amount);
    }

    @Nullable
    public static ItemStack createStar(@NotNull ItemStack holdingItem) {
        Stored stored = getStored(holdingItem);
        if (stored == null) {
            return null;
        }
        return InfiniteStar.createStar(stored);
    }

    @Nullable
    public static Stored getStored(@NotNull ItemStack holdingItem) {
        SlimefunItem slimefunItem = SlimefunItem.getByItem(holdingItem);
        if (slimefunItem != null) {
            if (!(slimefunItem instanceof NotPlaceable)) {
                return new SlimefunStored(slimefunItem);
            }
        } else if (StackUtils.itemsMatch(holdingItem, new ItemStack(holdingItem.getType()), true, false)) {
            return new VanillaStored(holdingItem.getType());
        }

        return null;
    }

    public static boolean isCombinedBlock(ItemStack itemStack) {
        return SlimefunItem.getByItem(itemStack) instanceof CombinedBlock;
    }

    public static @NotNull ItemStack getUnpackedItem(@NotNull ItemStack combined) {
        Stored stored = getStoredFromCombined(combined);
        if (stored == null) {
            return new ItemStack(Material.AIR);
        }
        long amount = getStoredAmountFromCombined(combined);
        if (amount <= 0) {
            return new ItemStack(Material.AIR);
        }
        return new CustomItemStack(stored.getItemStack(), (int) amount);
    }

    public static @NotNull ItemStack getUnpackedItemFromCombined(@NotNull ItemStack combined) {
        Stored stored = getStoredFromCombined(combined);
        if (stored == null) {
            return new ItemStack(Material.AIR);
        }

        // 检查
        if (isInfinity(combined)) {
            return new CustomItemStack(stored.getItemStack(), 576); // 返回物品
        }

        long amount = getStoredAmountFromCombined(combined);
        if (amount <= 0) {
            return new ItemStack(Material.AIR);
        }

        return new CustomItemStack(stored.getItemStack(), amount > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) amount);
    }

    public static boolean isInfiniteStar(ItemStack itemStack) {
        return SlimefunItem.getByItem(itemStack) instanceof InfiniteStar;
    }

    public static @NotNull ItemStack getUnpackedItemFromStar(@NotNull ItemStack star) {
        Stored stored = InfiniteStar.getStoredFromStar(star);
        if (stored == null) {
            return new ItemStack(Material.AIR);
        }
        return new CustomItemStack(stored.getItemStack(), 1);
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
            lore.set(1, ChatColor.translateAlternateColorCodes('&', "&e❃ &d剩余: " + getStoredFromCombined(itemStack).getName() + "&6 x" + Constants.INFINITY_STRING));
        } else {
            lore.set(1, ChatColor.translateAlternateColorCodes('&', "&e❃ &d剩余: " + getStoredFromCombined(itemStack).getName() + "&6 x" + getStoredAmountFromCombined(itemStack)));
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    public static void updateActionBarForPlayer(@NotNull Player player, @NotNull Stored stored, long amount) {
        player.sendActionBar(Component.text("❃ 剩余: " + stored.getName() + " x" + (isInfinity(amount) ? Constants.INFINITY_STRING : amount)));
    }

    public static boolean isInfinity(@NotNull ItemStack itemStack) {
        return getStoredAmountFromCombined(itemStack) >= Long.MAX_VALUE / 2;
    }

    public static boolean isInfinity(long amount) {
        return amount >= Long.MAX_VALUE / 2;
    }
}
