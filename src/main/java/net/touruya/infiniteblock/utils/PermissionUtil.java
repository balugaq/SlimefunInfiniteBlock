package net.touruya.infiniteblock.utils;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import net.touruya.infiniteblock.api.objects.SubCommand;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import javax.annotation.Nonnull;

public class PermissionUtil {
    public static final Interaction[] INTERACTIONS = {
            Interaction.PLACE_BLOCK,
            Interaction.BREAK_BLOCK,
            Interaction.INTERACT_BLOCK
    };

    public static boolean hasPermission(@Nonnull CommandSender sender, @Nonnull String permission) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }

        if (sender.isOp() || sender.hasPermission(Constants.PERMISSION_ADMIN) || sender.hasPermission(Constants.PERMISSION_COMMAND_ADMIN) || sender.hasPermission(permission)) {
            return true;
        }

        return false;
    }

    public static boolean hasPermission(@Nonnull CommandSender sender, @Nonnull SubCommand subCommand) {
        return hasPermission(sender, "slimefuninfiniteblock.command." + subCommand.getKey().toLowerCase());
    }

    public static boolean hasPermission(@Nonnull OfflinePlayer player, @Nonnull Block block) {
        return hasPermission(player, block.getLocation());
    }

    public static boolean hasPermission(@Nonnull OfflinePlayer player, @Nonnull Location location) {
        for (Interaction interaction : INTERACTIONS) {
            if (!hasPermission(player, location, interaction)) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasPermission(@Nonnull OfflinePlayer player, @Nonnull Block block, @Nonnull Interaction interaction) {
        return hasPermission(player, block.getLocation(), interaction);
    }

    public static boolean hasPermission(@Nonnull OfflinePlayer player, @Nonnull Location location, @Nonnull Interaction interaction) {
        return Slimefun.getProtectionManager().hasPermission(player, location, interaction);
    }
}
