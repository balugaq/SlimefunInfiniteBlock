package net.touruya.infiniteblock.utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import net.touruya.infiniteblock.api.stored.SlimefunStored;
import net.touruya.infiniteblock.api.stored.Stored;
import net.touruya.infiniteblock.api.stored.VanillaStored;
import net.touruya.infiniteblock.implementation.items.CombinedBlock;
import net.touruya.infiniteblock.implementation.items.InfiniteStar;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            return new SlimefunStored(slimefunItem);
        } else if (StackUtils.itemsMatch(holdingItem, new ItemStack(holdingItem.getType()), true, false)) {
            return new VanillaStored(holdingItem.getType());
        } else {
            return null;
        }
    }
}
