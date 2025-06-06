package net.touruya.infiniteblock.core.managers;

import lombok.Getter;
import net.touruya.infiniteblock.core.listeners.BlockListener;
import net.touruya.infiniteblock.core.listeners.CraftListener;
import net.touruya.infiniteblock.core.listeners.StarListener;
import net.touruya.infiniteblock.implementation.InfiniteBlocks;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ListenerManager {
    private final InfiniteBlocks plugin;
    private final @NotNull List<Listener> listeners = new ArrayList<>();

    public ListenerManager(InfiniteBlocks plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        listeners.add(new BlockListener(plugin));
        listeners.add(new StarListener(plugin));
        listeners.add(new CraftListener(plugin));
        registerListeners();
    }

    public void registerListeners() {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
}
