package net.touruya.infiniteblock.core.managers;

import lombok.Getter;
import net.touruya.infiniteblock.implementation.InfiniteBlocks;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * // data.yml
 * player_name:
 * count:
 * sfId: 12345 # 玩家总共破坏的方块数量，支持粘液方块id
 * material: 4040 # 玩家总共破坏的方块数量，支持原版material
 */
@Getter
public class PlayerDataManager {
    private final InfiniteBlocks plugin;
    private final File file;
    private YamlConfiguration snapshot;

    public PlayerDataManager(InfiniteBlocks plugin, File file) {
        this.plugin = plugin;
        this.file = file;
        loadData();
    }

    public static PlayerDataManager instance() {
        return InfiniteBlocks.getInstance().getPlayerDataManager();
    }

    public void saveData() {
        // 将 snapshot 写入 data.yml
        try {
            snapshot.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            snapshot = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSlimefunIdCount(@NotNull String playerName, @NotNull String sfId, int count) {
        if (!snapshot.contains(playerName)) {
            snapshot.createSection(playerName).createSection("count");
        }

        snapshot.getConfigurationSection(playerName).getConfigurationSection("count").set(sfId, count);
    }

    public void setMaterialCount(@NotNull String playerName, @NotNull String material, int count) {
        if (!snapshot.contains(playerName)) {
            snapshot.createSection(playerName).createSection("count");
        }

        snapshot.getConfigurationSection(playerName).getConfigurationSection("count").set(material, count);
    }

    public int getCurrentCount(@NotNull String playerName, @NotNull String key) {
        if (!snapshot.contains(playerName)) {
            return 0;
        }

        ConfigurationSection section = snapshot.getConfigurationSection(playerName).getConfigurationSection("count");
        if (section == null) {
            return 0;
        }

        if (!section.contains(key)) {
            return 0;
        }

        return section.getInt(key);
    }

    public void resetCount(@NotNull String playerName) {
        if (!snapshot.contains(playerName)) {
            return;
        }

        snapshot.getConfigurationSection(playerName).set("count", null);
    }

    public void addSlimefunIdCount(@NotNull String playerName, @NotNull String sfId, int count) {
        int currentCount = getCurrentCount(playerName, sfId);
        setSlimefunIdCount(playerName, sfId, currentCount + count);
    }

    public void addMaterialCount(@NotNull String playerName, @NotNull String material, int count) {
        int currentCount = getCurrentCount(playerName, material);
        setMaterialCount(playerName, material, currentCount + count);
    }
}
