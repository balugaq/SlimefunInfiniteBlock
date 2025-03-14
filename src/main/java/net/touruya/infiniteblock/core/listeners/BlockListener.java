package net.touruya.infiniteblock.core.listeners;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import lombok.Getter;
import net.touruya.infiniteblock.implementation.InfiniteBlocks;
import net.touruya.infiniteblock.implementation.items.CombinedBlock;
import net.touruya.infiniteblock.core.managers.PlayerDataManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

@Getter
public class BlockListener implements Listener {

    private final InfiniteBlocks plugin;

    public BlockListener(InfiniteBlocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCombinedBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Location location = e.getBlockPlaced().getLocation();
        ItemStack item = e.getItemInHand();
        SlimefunItem sfItem = SlimefunItem.getByItem(item);

        if (sfItem instanceof CombinedBlock combinedBlock) {
            e.setCancelled(true);
            combinedBlock.use(player, item, location);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void countBlock(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();
        SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(location);
        if (slimefunItem != null) {
            PlayerDataManager.instance().addSlimefunIdCount(event.getPlayer().getName(), slimefunItem.getId(), 1);
        } else {
            PlayerDataManager.instance().addMaterialCount(event.getPlayer().getName(), block.getType().name(), 1);
        }
    }
}
