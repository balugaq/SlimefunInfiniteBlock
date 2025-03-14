package net.touruya.infiniteblock.implementation.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.touruya.infiniteblock.implementation.InfiniteBlocks;
import net.touruya.infiniteblock.api.stored.SlimefunStored;
import net.touruya.infiniteblock.api.stored.Stored;
import net.touruya.infiniteblock.api.stored.VanillaStored;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.List;

public class CombinedBlock extends SlimefunItem {
    public static final int INFINITY_THRESHOLD = 1_000_000;
    public static final String INFINITY_STRING = "∞";
    private static CombinedBlock instance;
    private static final NamespacedKey STORED_KEY = new NamespacedKey(InfiniteBlocks.getInstance(), "stored");
    private static final NamespacedKey STORED_AMOUNT_KEY = new NamespacedKey(InfiniteBlocks.getInstance(), "stored_amount");
    public CombinedBlock(ItemGroup itemGroup, SlimefunItemStack slimefunItemStack, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, slimefunItemStack, recipeType, recipe);
        instance = this;
        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent blockPlaceEvent) {
            }
        });
    }

    public void use(Player player, ItemStack itemStack, Location location) {
        Stored stored = getStored(itemStack);
        if (stored == null) {
            return;
        }

        ItemStack storedItem = stored.getItemStack();
        if (!storedItem.getType().isBlock()) {
            return;
        }

        placeBlock(player, storedItem, stored, location);
        updateLore(itemStack);
    }

    public void placeBlock(Player player, ItemStack itemStack, Stored stored, Location location) {
        long currentAmount = getStoredAmount(itemStack);
        if (currentAmount <= 0) {
            player.sendMessage("融合方块已用完");
            return;
        }

        location.getBlock().setType(itemStack.getType());
        if (!isInfinity(itemStack)) {
            writePDC(itemStack, stored, currentAmount - 1);
        }
    }

    public static ItemStack createItemStack(Stored stored, long amount) {
        ItemStack clone = instance.getItem().clone();

        writePDC(clone, stored, amount);
        clone.setType(stored.getItemStack().getType());
        updateLore(clone);

        return clone;
    }

    public static void writePDC(ItemStack itemStack, Stored stored, long amount) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(STORED_KEY, PersistentDataType.STRING, stored.getIdentifier());
        meta.getPersistentDataContainer().set(STORED_AMOUNT_KEY, PersistentDataType.LONG, amount);
        Slimefun.getItemDataService().setItemData(itemStack, instance.getId());
        itemStack.setItemMeta(meta);
    }

    public static Stored getStored(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        String identifier = meta.getPersistentDataContainer().getOrDefault(STORED_KEY, PersistentDataType.STRING, "");
        Stored stored;
        stored = VanillaStored.loadFromIdentifier(identifier);
        if (stored == null) {
            stored = SlimefunStored.loadFromIdentifier(identifier);
        }

        return stored;
    }

    public static long getStoredAmount(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return meta.getPersistentDataContainer().getOrDefault(STORED_AMOUNT_KEY, PersistentDataType.LONG, 0L);
    }

    public static void updateLore(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            return;
        }

        if (lore.size() < 2) {
            return;
        }

        if (isInfinity(itemStack)) {
            lore.set(1, "已存储: " + getStored(itemStack).getName() + "x " + INFINITY_STRING);
        } else {
            lore.set(1, "已存储: " + getStored(itemStack).getName() + "x " + getStoredAmount(itemStack));
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    public static boolean isInfinity(ItemStack itemStack) {
        return getStoredAmount(itemStack) >= INFINITY_THRESHOLD;
    }
}
