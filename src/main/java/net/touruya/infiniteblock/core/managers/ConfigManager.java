package net.touruya.infiniteblock.core.managers;

import net.touruya.infiniteblock.implementation.InfiniteBlocks;
import net.touruya.infiniteblock.utils.Constants;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ConfigManager {
    @Nonnull
    private final InfiniteBlocks plugin;

    public ConfigManager(@Nonnull InfiniteBlocks plugin) {
        this.plugin = plugin;

        setupDefaultConfig();
    }

    public static ConfigManager instance() {
        return Constants.PLUGIN.getConfigManager();
    }

    private void setupDefaultConfig() {
        // config.yml
        final InputStream inputStream = this.plugin.getResource("config.yml");
        final File existingFile = new File(this.plugin.getDataFolder(), "config.yml");

        if (inputStream == null) {
            return;
        }

        final Reader reader = new InputStreamReader(inputStream);
        final FileConfiguration resourceConfig = YamlConfiguration.loadConfiguration(reader);
        final FileConfiguration existingConfig = YamlConfiguration.loadConfiguration(existingFile);

        for (String key : resourceConfig.getKeys(false)) {
            checkKey(existingConfig, resourceConfig, key);
        }

        try {
            existingConfig.save(existingFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ParametersAreNonnullByDefault
    private void checkKey(@Nonnull FileConfiguration existingConfig, @Nonnull FileConfiguration resourceConfig, @Nonnull String key) {
        final Object currentValue = existingConfig.get(key);
        final Object newValue = resourceConfig.get(key);
        if (newValue instanceof ConfigurationSection section) {
            for (String sectionKey : section.getKeys(false)) {
                checkKey(existingConfig, resourceConfig, key + "." + sectionKey);
            }
        } else if (currentValue == null) {
            existingConfig.set(key, newValue);
        }
    }

    public long getInfinityThreshold() {
        return plugin.getConfig().getLong("infinity-threshold", Long.MAX_VALUE / 2);
    }

    public int getAutosavePeriod() {
        return plugin.getConfig().getInt("autosave-period", 300);
    }

    public int getStarThreshold() {
        return plugin.getConfig().getInt("star-threshold", 100_000);
    }

    public void setConfig(@Nonnull String path, Object value) {
        plugin.getConfig().set(path, value);
        plugin.saveConfig();
    }
}
