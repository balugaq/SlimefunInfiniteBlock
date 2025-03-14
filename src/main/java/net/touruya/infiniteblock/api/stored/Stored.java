package net.touruya.infiniteblock.api.stored;

import org.bukkit.inventory.ItemStack;

public interface Stored {
    String getName();

    ItemStack getItemStack();
    String getIdentifier();
}
