package net.touruya.infiniteblock.api.stored;

import io.github.thebusybiscuit.slimefun4.core.attributes.DistinctiveItem;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

public interface NotDupeable extends DistinctiveItem {
    default boolean canStack(@Nonnull ItemMeta meta1, @Nonnull ItemMeta meta2) {
        return meta1.getPersistentDataContainer().equals(meta2.getPersistentDataContainer());
    }
}
