package net.touruya.infiniteblock.implementation;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import net.touruya.infiniteblock.core.commands.StarCommand;
import net.touruya.infiniteblock.core.commands.StorageCommand;
import net.touruya.infiniteblock.core.managers.ConfigManager;
import net.touruya.infiniteblock.core.managers.ListenerManager;
import net.touruya.infiniteblock.core.managers.PlayerDataManager;
import net.touruya.infiniteblock.utils.Constants;
import net.touruya.infiniteblock.utils.SlimefunItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class InfiniteBlocks extends JavaPlugin implements SlimefunAddon {
    private static final Runnable AUTOSAVE_TASK = () -> {
        PlayerDataManager.instance().saveData();
    };
    @Getter
    private static InfiniteBlocks instance;
    @Getter
    private ConfigManager configManager;
    @Getter
    private PlayerDataManager playerDataManager;
    @Getter
    private ListenerManager listenerManager;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);

        playerDataManager = new PlayerDataManager(this, Constants.DATA_FILE);

        SIBGroups.setup(this);
        SIBRecipeTypes.setup(this);
        SIBItems.setup(this);

        // 注册监听器
        listenerManager = new ListenerManager(this);
        listenerManager.setup();

        // 注册指令
        getCommand("sibstorage").setExecutor(new StorageCommand(this));
        getCommand("sibstar").setExecutor(new StarCommand(this));

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, AUTOSAVE_TASK, 1L, Constants.AUTOSAVE_PERIOD * 20L);
    }

    @Override
    public void onDisable() {
        PlayerDataManager.instance().saveData();
        SlimefunItemUtil.unregisterAllItems();
        SlimefunItemUtil.unregisterAllItemGroups();
    }

    @Override
    public @NotNull JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/balugaq/SlimefunInfiniteBlock/issues";
    }
}
