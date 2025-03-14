package net.touruya.infiniteblock.utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Material;

public class Icons {
    public static final SlimefunItemStack COMBINER =  new SlimefunItemStack(
            "BLOCK_OPERATE_MACHINE",
            Material.CRAFTING_TABLE,
            "&6方块操作台",
            "&e将方块融合为融合方块",
            "&e将 100 万个方块转换为无限使用的方块",
            "&7需要电力运行" // todo: 暂时没做
    );
}
