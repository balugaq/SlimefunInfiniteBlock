package net.touruya.infiniteblock.utils;

import lombok.experimental.UtilityClass;
import net.touruya.infiniteblock.core.managers.ConfigManager;
import net.touruya.infiniteblock.implementation.InfiniteBlocks;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

@UtilityClass
public class Constants {
    //<editor-fold desc="Plugin">
    public static final InfiniteBlocks PLUGIN = InfiniteBlocks.getInstance();
    //</editor-fold>

    //<editor-fold desc="Permissions">
    public static final String PERMISSION_ADMIN = "slimefuninfiniteblock.admin";
    public static final String PERMISSION_COMMAND_ADMIN = "slimefuninfiniteblock.command.admin";
    public static final String PERMISSION_COMMAND_STORAGE = "slimefuninfiniteblock.command.storage";
    public static final String PERMISSION_COMMAND_STAR = "slimefuninfiniteblock.command.star";
    //</editor-fold>

    //<editor-fold desc="Symbols">
    public static final String INFINITY_STRING = "∞";
    //</editor-fold>

    //<editor-fold desc="Group keys">
    public static final NamespacedKey MATERIALS = new NamespacedKey(PLUGIN, "materials");
    public static final NamespacedKey MACHINES = new NamespacedKey(PLUGIN, "machines");
    public static final NamespacedKey INFINITE_GROUP = new NamespacedKey(PLUGIN, "infinite_blocks");
    //</editor-fold>

    //<editor-fold desc="Recipe type keys">
    public static final NamespacedKey COMBINER = new NamespacedKey(PLUGIN, "combiner");
    public static final NamespacedKey BREAK_BLOCK = new NamespacedKey(PLUGIN, "break_block");
    //</editor-fold>

    //<editor-fold desc="Item keys">
    public static final NamespacedKey IDENTIFIER_KEY = new NamespacedKey(PLUGIN, "identifier");
    public static final NamespacedKey STORED_KEY = new NamespacedKey(PLUGIN, "stored");
    public static final NamespacedKey STORED_AMOUNT_KEY = new NamespacedKey(PLUGIN, "stored_amount");
    //</editor-fold>

    //<editor-fold desc="Files">
    public static final File DATA_FILE = new File(PLUGIN.getDataFolder(), "data/data.yml");
    //</editor-fold>

    //<editor-fold desc="Config">
    public static final long INFINITY_THRESHOLD = ConfigManager.instance().getInfinityThreshold();
    public static final int AUTOSAVE_PERIOD = ConfigManager.instance().getAutosavePeriod();
    public static final int STAR_THRESHOLD = ConfigManager.instance().getStarThreshold();
    //</editor-fold>

    //<editor-fold desc="Descriptions">
    public static final List<ItemStack> DESCRIPTION_COMBINER = List.of(
            Icons.DESCRIPTION_COMBINER_1,new ItemStack(Material.AIR),Icons.DESCRIPTION_COMBINER_2,new ItemStack(Material.AIR),Icons.DESCRIPTION_COMBINER_3
    );

    public static final List<ItemStack> DESCRIPTION_ITEM_FOUNTAIN_BALANCER = List.of(
            Icons.DESCRIPTION_ITEM_FOUNTAIN_BALANCER_1
    );

    public static final List<ItemStack> DESCRIPTION_STAR_OPERATOR = List.of(
            Icons.DESCRIPTION_STAR_OPERATOR_1
    );
    //</editor-fold>
}
