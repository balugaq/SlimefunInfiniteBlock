package net.touruya.infiniteblock.utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import net.touruya.infiniteblock.core.managers.ConfigManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Icons {
    public static final SlimefunItemStack COMBINER = new SlimefunItemStack(
            "SIB_BLOCK_OPERATE_MACHINE",
            Material.CRAFTING_TABLE,
            "&6方块制造台",
            "&e无尽方块制造的地方"," ",
            "&a需要电力运行",
            "&e❁ &f可存储8192J",
            "&e❁ &f耗电量128J/t"
    );

    public static final SlimefunItemStack COMBINED_BLOCK = new SlimefunItemStack(
            "SIB_COMBINED_BLOCK",
            Material.GLASS,
            "&6无尽方块",
            "&e浓缩的方块？怎么放出来的不一样"," "
    );

    public static final SlimefunItemStack INFINITE_STAR = new SlimefunItemStack(
            "SIB_INFINITE_STAR",
            Material.NETHER_STAR,
            "&6无限之星",
            "&6用于在物质爆发平衡器中产出物质"," ",
            "&e❃ &6通过挖掘方块获得"
    );

    public static final SlimefunItemStack STAR_OPERATOR = new SlimefunItemStack(
            "SIB_STAR_OPERATOR",
            Material.CARTOGRAPHY_TABLE,
            "&6方块操作台",
            "&e用于制作无尽方块"
    );

    public static final SlimefunItemStack ITEM_FOUNTAIN_BALANCER = new SlimefunItemStack(
            "SIB_ITEM_FOUNTAIN_BALANCER",
            Material.IRON_BLOCK,
            "&6物品输出器",
            "&e通过无尽方块产出物品"," ",

            "&a需要电力运行",
            "&e❁ &f可存储8192J",
            "&e❁ &f耗电量128J/t"
    );
    public static final SlimefunItemStack ENITEM_FOUNTAIN_BALANCER = new SlimefunItemStack(
            "SIB_ENITEM_FOUNTAIN_BALANCER",
            Material.IRON_BLOCK,
            "&4终极物品输出器",
            "&e通过无尽方块产出物品"," ",

            "&a需要电力运行",
            "&e❁ &f可存储8192J",
            "&e❁ &f耗电量128J/t"
    );
    public static final ItemStack NESTED_GROUP = new CustomItemStack(Material.CHEST, "&6无限方块");
    public static final ItemStack MACHINES_GROUP = new CustomItemStack(Material.DISPENSER, "&6无限方块 - 机器");
    public static final ItemStack MATERIALS_GROUP = new CustomItemStack(Material.NETHER_STAR, "&6无限方块 - 材料");

    public static final ItemStack BREAK_BLOCK = new CustomItemStack(
            Material.DIAMOND_PICKAXE,
            "&c破坏方块",
            "&7通过破坏方块获得"
    );
    //无限之星
    public static final ItemStack INFINITE_STAR_LORE = new CustomItemStack(
            Material.KNOWLEDGE_BOOK,
            "&6机制-获取",
            "&e当破坏任意方块数量达" + Constants.STAR_THRESHOLD + "&e时","&e在背包发放对应物品的无限之星","&e背包无空位则掉落至地面","&b支持粘液科技方块,机器"
    );
    public static final ItemStack INFINITE_STAR_LORE1 = new CustomItemStack(
            Material.KNOWLEDGE_BOOK,
            "&f机制-&c限制",
            "&c当破坏的方块重新放置后会使破坏数量减少"
    );
    public static final ItemStack BACKGROUND = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " ", " ");
    public static final ItemStack INPUT_BORDER = new CustomItemStack(Material.BLUE_STAINED_GLASS_PANE, " ", " ");
    public static final ItemStack OUTPUT_BORDER = new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE, " ", " ");
    public static final ItemStack CRAFT = new CustomItemStack(Material.RED_STAINED_GLASS_PANE, "&a合成", " ");

    //<editor-fold desc="Description">
    //<editor-fold desc="Description - Combiner">
    //方块融合
    public static final ItemStack DESCRIPTION_COMBINER_1 = new CustomItemStack(
            Material.KNOWLEDGE_BOOK,
            "&6机制-制造",
            "&e任意方块在输入槽中会制造出无尽方块"
    );
    public static final ItemStack DESCRIPTION_COMBINER_2 = new CustomItemStack(
            Material.KNOWLEDGE_BOOK,
            "&6机制-添加",
            "&e当输出槽有无尽方块时输入任意方块会添加至输出的方块"
    );
    public static final ItemStack DESCRIPTION_COMBINER_3 = new CustomItemStack(
            Material.KNOWLEDGE_BOOK,
            "&6机制-融合",
            "&e当输入的是无尽方块时会融合数量至输出槽"
    );
    //</editor-fold>

    //<editor-fold desc="Description - Combined Block">
    //物品爆发
    public static final ItemStack DESCRIPTION_ITEM_FOUNTAIN_BALANCER_1 = new CustomItemStack(
            Material.KNOWLEDGE_BOOK,
            "&6机制-输出",
            "&e当输入有数量的任意无尽方块时，输出对应产物"
    );
    public static final ItemStack DESCRIPTION_ITEM_FOUNTAIN_BALANCER_2 = new CustomItemStack(
            Material.KNOWLEDGE_BOOK,
            "&c机制-限制",
            "&e当输入无限数量的任意无尽方块时，无法输出对应产物"
    );
    //</editor-fold>
    //<editor-fold desc="Description - Star Operator">
    //方块操作
    public static final ItemStack DESCRIPTION_STAR_OPERATOR_1 = new CustomItemStack(
            Material.KNOWLEDGE_BOOK,
            "&6机制-合成",
            "&e当无尽方块剩余数量>=1000000时","&e在左侧放入无尽方块右侧放入对应物品的无限之星","&e可合成剩余数量:∞的无尽方块"
    );

    //</editor-fold>
    //</editor-fold>
}
