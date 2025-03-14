package net.touruya.infiniteblock.utils;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.experimental.UtilityClass;
import net.touruya.infiniteblock.implementation.InfiniteBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class SlimefunItemUtil {
    public static void unregisterItem(SlimefunItem item) {
        if (item instanceof Radioactive) {
            Slimefun.getRegistry().getRadioactiveItems().remove(item);
        }

        if (item instanceof GEOResource geor) {
            Slimefun.getRegistry().getGEOResources().remove(geor.getKey());
        }

        Slimefun.getRegistry().getTickerBlocks().remove(item.getId());
        Slimefun.getRegistry().getEnabledSlimefunItems().remove(item);

        Slimefun.getRegistry().getSlimefunItemIds().remove(item.getId());
        Slimefun.getRegistry().getAllSlimefunItems().remove(item);
        Slimefun.getRegistry().getMenuPresets().remove(item.getId());
        Slimefun.getRegistry().getBarteringDrops().remove(item.getItem());
    }

    public static void unregisterAllItems() {
        List<SlimefunItem> items = new ArrayList<>(Slimefun.getRegistry().getAllSlimefunItems());
        for (SlimefunItem item : items) {
            if (item.getAddon() instanceof InfiniteBlocks) {
                unregisterItem(item);
            }
        }
    }

    public static void unregisterAllItemGroups() {
        for (ItemGroup itemGroup : Slimefun.getRegistry().getAllItemGroups()) {
            if (itemGroup.getAddon() instanceof InfiniteBlocks) {
                unregisterItemGroup(itemGroup);
            }
        }
    }

    public static void unregisterItemGroup(@NotNull ItemGroup itemGroup) {
        if (itemGroup == null) {
            return;
        }

        Slimefun.getRegistry().getAllItemGroups().remove(itemGroup);
    }
}
