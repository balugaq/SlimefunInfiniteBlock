package net.touruya.infiniteblock.core.managers;

import lombok.Getter;
import net.touruya.infiniteblock.implementation.InfiniteBlocks;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * // data.yml
 * player_name:
 *   count:
 *     sfId: 12345 # 玩家总共破坏的方块数量，支持粘液方块id
 *     material: 4040 # 玩家总共破坏的方块数量，支持原版material
 */
@Getter
public class PlayerDataManager {
    public static PlayerDataManager instance() {
        return InfiniteBlocks.getInstance().getPlayerDataManager();
    }

    private InfiniteBlocks plugin;
    private File file;
    private YamlConfiguration snapshot;
    public PlayerDataManager(InfiniteBlocks plugin, File file) {
        this.plugin = plugin;
        loadData();
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
        // 从 data.yml 读取数据，存储到 snapshot 中
        try {
            snapshot = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSlimefunIdCount(String playerName, String sfId, int count) {
        if (!snapshot.contains(playerName)) {
            snapshot.createSection(playerName).createSection("count");
        }

        snapshot.getConfigurationSection(playerName).getConfigurationSection("count").set(sfId, count);
    }

    public void setMaterialCount(String playerName, String material, int count) {
        if (!snapshot.contains(playerName)) {
            snapshot.createSection(playerName).createSection("count");
        }

        snapshot.getConfigurationSection(playerName).getConfigurationSection("count").set(material, count);
    }

    public int getCurrentCount(String playerName, String key) {
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

    public void resetCount(String playerName) {
        if (!snapshot.contains(playerName)) {
            return;
        }

        snapshot.getConfigurationSection(playerName).set("count", null);
    }

    public void addSlimefunIdCount(String playerName, String sfId, int count) {
        int currentCount = getCurrentCount(playerName, sfId);
        setSlimefunIdCount(playerName, sfId, currentCount + count);
    }

    public void addMaterialCount(String playerName, String material, int count) {
        int currentCount = getCurrentCount(playerName, material);
        setMaterialCount(playerName, material, currentCount + count);
    }
}
