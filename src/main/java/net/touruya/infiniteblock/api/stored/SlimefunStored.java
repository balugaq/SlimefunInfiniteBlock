package net.touruya.infiniteblock.api.stored;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class SlimefunStored implements Stored {
    private final SlimefunItem item;

    public SlimefunStored(SlimefunItem item) {
        this.item = item;
    }

    public static @Nullable SlimefunStored loadFromIdentifier(@NotNull String identifier) {
        String[] parts = identifier.split(":");
        if (parts.length != 2 || !parts[0].equals("slimefun")) {
            return null;
        }
        SlimefunItem item = SlimefunItem.getById(parts[1]);
        if (item == null) {
            return null;
        }
        return new SlimefunStored(item);
    }

    @Override
    public @NotNull String getName() {
        return item.getItemName();
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        return item.getItem();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "slimefun:" + getName();
    }
}
