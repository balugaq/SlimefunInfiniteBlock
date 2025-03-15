package net.touruya.infiniteblock.core.commands;

import lombok.Getter;
import net.touruya.infiniteblock.api.objects.SubCommand;
import net.touruya.infiniteblock.implementation.InfiniteBlocks;
import net.touruya.infiniteblock.utils.PermissionUtil;
import net.touruya.infiniteblock.utils.StoredUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@Getter
public class StarCommand extends SubCommand {
    private static final String KEY = "star";
    @Nonnull
    private final InfiniteBlocks plugin;

    public StarCommand(@Nonnull InfiniteBlocks plugin) {
        this.plugin = plugin;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!PermissionUtil.hasPermission(commandSender, this)) {
            commandSender.sendMessage("你没有权限使用这个指令");
            return false;
        }

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("只有玩家可以执行这个指令");
            return false;
        }

        ItemStack holdingItem = player.getInventory().getItemInMainHand();
        if (holdingItem == null || holdingItem.getType() == Material.AIR) {
            player.sendMessage("你必须持有物品才能执行这个指令");
        }

        if (!holdingItem.getType().isBlock()) {
            player.sendMessage("你必须持有方块才能执行这个指令");
        }

        player.getInventory().addItem(StoredUtils.createStar(holdingItem));

        return true;
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        return new ArrayList<>();
    }

    @Override
    @Nonnull
    public String getKey() {
        return KEY;
    }
}
