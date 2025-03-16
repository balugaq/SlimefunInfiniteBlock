package net.touruya.infiniteblock.utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Icons {
    public static final SlimefunItemStack COMBINER = new SlimefunItemStack(
            "SIB_BLOCK_OPERATE_MACHINE",
            Material.CRAFTING_TABLE,
            "&6方块融合台",
            "&e将方块融合为融合方块",
            "&e需要电力运行"
    );

    public static final SlimefunItemStack COMBINED_BLOCK = new SlimefunItemStack(
            "SIB_COMBINED_BLOCK",
            Material.BEDROCK,
            "&6融合方块",
            "&e将大量方块融合为 1 个方块",
            "&e已存储: "
    );

    public static final SlimefunItemStack INFINITE_STAR = new SlimefunItemStack(
            "SIB_INFINITE_STAR",
            Material.NETHER_STAR,
            "&6无限之星",
            "&6用于在物质爆发平衡器中产出物质",
            "&6通过挖掘方块获得"
    );

    public static final SlimefunItemStack STAR_OPERATOR = new SlimefunItemStack(
            "SIB_STAR_OPERATOR",
            Material.CARTOGRAPHY_TABLE,
            "&6操作台",
            "&e用于制作无限融合方块"
    );

    public static final SlimefunItemStack ITEM_FOUNTAIN_BALANCER = new SlimefunItemStack(
            "SIB_ITEM_FOUNTAIN_BALANCER",
            Material.IRON_BLOCK,
            "&6物品爆发平衡器",
            "&e产出物品",
            "&e需要电力运行"
    );

    public static final ItemStack NESTED_GROUP = new CustomItemStack(Material.CHEST, "&6无限方块");
    public static final ItemStack MACHINES_GROUP = new CustomItemStack(Material.DISPENSER, "&6无限方块 - 机器");
    public static final ItemStack MATERIALS_GROUP = new CustomItemStack(Material.NETHER_STAR, "&6无限方块 - 材料");

    public static final ItemStack BREAK_BLOCK = new CustomItemStack(
            Material.DIAMOND_PICKAXE,
            "&c破坏方块",
            "&7通过破坏方块获得"
    );

    public static final ItemStack BACKGROUND = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " ", " ");
    public static final ItemStack INPUT_BORDER = new CustomItemStack(Material.BLUE_STAINED_GLASS_PANE, " ", " ");
    public static final ItemStack OUTPUT_BORDER = new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE, " ", " ");
    public static final ItemStack CRAFT = new CustomItemStack(Material.RED_STAINED_GLASS_PANE, "&a合成", " ");

    //<editor-fold desc="Description">
    //<editor-fold desc="Description - Combiner">
    public static final ItemStack DESCRIPTION_COMBINER_1 = new CustomItemStack(
            Material.KNOWLEDGE_BOOK,
            "&6描述",
            "&e描述"
    );
    //</editor-fold>

    //<editor-fold desc="Description - Combined Block">
    public static final ItemStack DESCRIPTION_ITEM_FOUNTAIN_BALANCER_1 = new CustomItemStack(
            Material.KNOWLEDGE_BOOK,
            "&6描述",
            "&e描述"
    );
    //</editor-fold>

    //<editor-fold desc="Description - Star Operator">
    public static final ItemStack DESCRIPTION_STAR_OPERATOR_1 = new CustomItemStack(
            Material.KNOWLEDGE_BOOK,
            "&6描述",
            "&e描述"
    );
    //</editor-fold>
    //</editor-fold>
}
