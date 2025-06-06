package net.touruya.infiniteblock.core.listeners;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import lombok.Getter;
import net.touruya.infiniteblock.implementation.InfiniteBlocks;
import net.touruya.infiniteblock.implementation.items.CombinedBlock;
import net.touruya.infiniteblock.utils.StoredUtils;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

@Getter
public class CraftListener implements Listener {
    private final InfiniteBlocks plugin;

    public CraftListener(InfiniteBlocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public static void craft(CraftItemEvent event) {
        boolean hasCombined = false;
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (SlimefunItem.getByItem(item) instanceof CombinedBlock) {
                if (item.getAmount() > 1) {
                    event.setResult(Event.Result.DENY);
                    event.setCurrentItem(null);
                    return;
                }

                hasCombined = true;
            }
        }

        if (!hasCombined) {
            return;
        }

        event.setResult(Event.Result.DENY);
        ItemStack[] newMatrix = new ItemStack[event.getInventory().getMatrix().length];
        int index = 0;
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item != null) {
                if (SlimefunItem.getByItem(item) instanceof CombinedBlock) {
                    var stored = StoredUtils.getStored(item);
                    var amount = StoredUtils.getStoredAmountFromCombined(item);
                    if (amount == 1) {
                        item.setAmount(0);
                        newMatrix[index] = item;
                    } else {
                        newMatrix[index] = CombinedBlock.createCombined(stored, amount - 1);
                    }
                } else {
                    item.setAmount(item.getAmount() - 1);
                    newMatrix[index] = item;
                }
            }

            index++;
        }

        event.getInventory().setMatrix(newMatrix);
        event.setCursor(event.getRecipe().getResult().clone());
    }
}
