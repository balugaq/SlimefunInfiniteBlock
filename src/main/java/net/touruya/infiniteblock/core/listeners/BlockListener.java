package net.touruya.infiniteblock.core.listeners;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.Getter;
import net.touruya.infiniteblock.api.stored.SlimefunStored;
import net.touruya.infiniteblock.api.stored.Stored;
import net.touruya.infiniteblock.implementation.InfiniteBlocks;
import net.touruya.infiniteblock.implementation.items.CombinedBlock;
import net.touruya.infiniteblock.utils.StoredUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public class BlockListener implements Listener {

    private final InfiniteBlocks plugin;

    public BlockListener(InfiniteBlocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCombinedBlockPlace(@NotNull BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Location location = e.getBlockPlaced().getLocation();
        final ItemStack itemStack = e.getItemInHand().clone();
        SlimefunItem sfItem = SlimefunItem.getByItem(itemStack);
        Block placedAgainst = e.getBlockAgainst();
        BlockState replacedBlockState = e.getBlockReplacedState();

        if (sfItem instanceof CombinedBlock combinedBlock) {
            if (StorageCacheUtils.getSfItem(location) == null) {
                final long currentAmount = StoredUtils.getStoredAmountFromCombined(itemStack);
                if (currentAmount <= 0) {
                    player.sendMessage("融合方块已用完");
                    e.setCancelled(true);
                    return;
                }
                if (itemStack.getAmount() != 1) {
                    player.sendMessage("你只能手持一个融合方块");
                    e.setCancelled(true);
                    return;
                }

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Slimefun.getDatabaseManager().getBlockDataController().removeBlock(location);
                    Stored stored = combinedBlock.use(player, itemStack, location);
                    player.getInventory().setItem(e.getHand(), itemStack);
                    if (stored instanceof SlimefunStored ss) {
                        SlimefunItem slimefunItem = ss.getItem();
                        slimefunItem.callItemHandler(BlockPlaceHandler.class, handler -> handler.onPlayerPlace(
                                new BlockPlaceEvent(
                                        location.getBlock(),
                                        replacedBlockState,
                                        placedAgainst,
                                        itemStack,
                                        player,
                                        e.canBuild(),
                                        e.getHand()
                                )
                        ));
                    }
                }, 1L);
            }
        }
    }
}
