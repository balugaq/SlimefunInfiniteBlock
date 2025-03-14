package net.touruya.infiniteblock.implementation;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import lombok.Getter;
import net.touruya.infiniteblock.core.commands.StorageCommand;
import net.touruya.infiniteblock.core.managers.ConfigManager;
import net.touruya.infiniteblock.core.managers.ListenerManager;
import net.touruya.infiniteblock.core.managers.PlayerDataManager;
import net.touruya.infiniteblock.implementation.items.CombinedBlock;
import net.touruya.infiniteblock.implementation.items.Combiner;
import net.touruya.infiniteblock.utils.Constants;
import net.touruya.infiniteblock.utils.SlimefunItemUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class InfiniteBlocks extends JavaPlugin implements SlimefunAddon {
    @Getter
    private static InfiniteBlocks instance;
    @Getter
    private ConfigManager configManager;
    @Getter
    private PlayerDataManager playerDataManager;
    @Getter
    private ListenerManager listenerManager;

    private NestedItemGroup nestedGroup;
    private SubItemGroup machinesGroup;
    private SubItemGroup materialsGroup;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);

        playerDataManager = new PlayerDataManager(this, Constants.DATA_FILE);

        nestedGroup = new NestedItemGroup(
                new NamespacedKey(this, "infinite_blocks"),
                new CustomItemStack(Material.CHEST, "&6无限方块")
        );

        // 创建机器子组
        machinesGroup = new SubItemGroup(
                new NamespacedKey(this, "machines"),
                nestedGroup,
                new CustomItemStack(Material.DISPENSER, "&6无限方块 - 机器")
        );

        // 创建材料子组
        materialsGroup = new SubItemGroup(
                new NamespacedKey(this, "materials"),
                nestedGroup,
                new CustomItemStack(Material.NETHER_STAR, "&6无限方块 - 材料")
        );

        nestedGroup.register(this);
        // 注册子组
        machinesGroup.register(this);
        materialsGroup.register(this);

        // 注册监听器
        listenerManager = new ListenerManager(this);
        listenerManager.setup();

        // 注册指令
        getCommand("sibstorage").setExecutor(new StorageCommand(this));

        // 注册物品
        registerItems();
    }

    private void registerItems() {
        // 注册机器
        new Combiner(
                machinesGroup,
                new SlimefunItemStack(
                        "BLOCK_OPERATE_MACHINE",
                        Material.CRAFTING_TABLE,
                        "&6方块操作台",
                        "&e将方块融合为融合方块",
                        "&e将 100 万个方块转换为无限使用的方块",
                        "&7需要电力运行" // todo: 暂时没做
                ),
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR,
                        SlimefunItems.CARBONADO, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.CARBONADO,
                        SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO, SlimefunItems.ELECTRIC_MOTOR
                }
        ).register(this);

        new CombinedBlock(
                materialsGroup,
                new SlimefunItemStack(
                        "COMBINED_BLOCK",
                        Material.BEDROCK,
                        "&6融合方块",
                        "&e将大量方块融合为 1 个方块",
                        "&e已存储: "
                ),
                RecipeType.NULL,
                new ItemStack[0]
        ).register(this);
    }

    @Override
    public void onDisable() {
        PlayerDataManager.instance().saveData(); // todo: 定时保存
        SlimefunItemUtil.unregisterAllItems();
        SlimefunItemUtil.unregisterAllItemGroups();
    }

    @Override
    public @NotNull JavaPlugin getJavaPlugin() {
        return this;
    }

    // todo: 这里填写你的仓库地址
    @Override
    public String getBugTrackerURL() {
        return "https://github.com/balugaq/SlimefunInfiniteBlock/issues";
    }
}
