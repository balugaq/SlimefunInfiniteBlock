package net.touruya.infiniteblock.api.stored;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.inventory.ItemStack;

public class SlimefunStored implements Stored {
    private final SlimefunItem item;
    public SlimefunStored(SlimefunItem item) {
        this.item = item;
    }

    @Override
    public String getName() {
        return item.getItemName();
    }

    @Override
    public ItemStack getItemStack() {
        return item.getItem();
    }

    @Override
    public String getIdentifier() {
        return "slimefun:" + getName();
    }

    public static SlimefunStored loadFromIdentifier(String identifier) {
        String[] parts = identifier.split(":");
        if (parts.length!= 2 ||!parts[0].equals("slimefun")) {
            return null;
        }
        SlimefunItem item = SlimefunItem.getById(parts[1]);
        if (item == null) {
            return null;
        }
        return new SlimefunStored(item);
    }
}
