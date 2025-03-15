package net.touruya.infiniteblock.utils;

import net.touruya.infiniteblock.api.objects.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import javax.annotation.Nonnull;

public class PermissionUtil {
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
}
