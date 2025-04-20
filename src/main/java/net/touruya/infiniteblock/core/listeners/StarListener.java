package net.touruya.infiniteblock.core.listeners;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.touruya.infiniteblock.api.stored.Stored;
import net.touruya.infiniteblock.core.managers.PlayerDataManager;
import net.touruya.infiniteblock.implementation.InfiniteBlocks;
import net.touruya.infiniteblock.implementation.items.InfiniteStar;
import net.touruya.infiniteblock.utils.Constants;
import net.touruya.infiniteblock.utils.StoredUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public class StarListener implements Listener {
    private final PlayerDataManager manager;
    private final InfiniteBlocks plugin;

    public StarListener(InfiniteBlocks plugin) {
        this.plugin = plugin;
        this.manager = PlayerDataManager.instance();
    }

    @EventHandler(ignoreCancelled = true)
    public void countBlock(@NotNull BlockBreakEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();
        SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(location);
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (slimefunItem != null) {
            String key = slimefunItem.getId();
            int currentCount = manager.addSlimefunIdCount(playerName, key, 1);
            if (currentCount % Constants.STAR_THRESHOLD == 0) {
                int gave = manager.getSlimefunStarCount(playerName, key);
                if (currentCount / Constants.STAR_THRESHOLD > gave) {
                    manager.addSlimefunStarCount(playerName, key, 1);
                    giveStar(player, slimefunItem, 1);
                }
            }
        } else {
            Material material = block.getType();
            String key = material.name();
            int currentCount = manager.addMaterialCount(playerName, key, 1);
            if (currentCount % Constants.STAR_THRESHOLD == 0) {
                int gave = manager.getMaterialStarCount(playerName, key);
                if (currentCount / Constants.STAR_THRESHOLD > gave) {
                    manager.addMaterialStarCount(playerName, key, 1);
                    giveStar(player, material, 1);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void uncountBlock(@NotNull BlockPlaceEvent event) {
        Block block = event.getBlock();
        SlimefunItem slimefunItem = SlimefunItem.getByItem(event.getItemInHand());
        String playerName = event.getPlayer().getName();
        if (slimefunItem != null) {
            manager.reduceSlimefunIdCount(playerName, slimefunItem.getId(), 1);
        } else {
            manager.reduceMaterialCount(playerName, block.getType().name(), 1);
        }
    }

    private void giveStar(@NotNull Player player, @NotNull Material material, int amount) {
        Stored stored = StoredUtils.getStored(new ItemStack(material));
        if (stored == null) {
            return;
        }
        giveStar(player, stored, amount);
    }

    private void giveStar(@NotNull Player player, @NotNull SlimefunItem slimefunItem, int amount) {
        Stored stored = StoredUtils.getStored(slimefunItem.getItem());
        if (stored == null) {
            return;
        }
        giveStar(player, stored, amount);
    }

    private void giveStar(@NotNull Player player, @NotNull Stored stored, int amount) {
        ItemStack star = InfiniteStar.createStar(stored);
        star.setAmount(amount);
        var left = player.getInventory().addItem(star);
        if (left != null && !left.isEmpty()) {
            player.getWorld().dropItemNaturally(player.getLocation(), star);
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6你获取了 " + amount + " 个无限之星!"));
    }
}
