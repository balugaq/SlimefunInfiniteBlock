package net.touruya.infiniteblock.implementation.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import net.md_5.bungee.api.ChatColor;
import net.touruya.infiniteblock.api.stored.SlimefunStored;
import net.touruya.infiniteblock.api.stored.Stored;
import net.touruya.infiniteblock.api.stored.VanillaStored;
import net.touruya.infiniteblock.utils.Constants;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class InfiniteStar extends SlimefunItem implements NotPlaceable {
    private static InfiniteStar instance;
    private static String name;
    private static String[] lore;

    public InfiniteStar(@NotNull ItemGroup itemGroup, @NotNull SlimefunItemStack slimefunItemStack, @NotNull RecipeType recipeType, ItemStack @NotNull [] recipe) {
        super(itemGroup, slimefunItemStack, recipeType, recipe);
        instance = this;
        name = instance.getItemName();
        lore = instance.getItem().getItemMeta().getLore().toArray(new String[0]);
        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent blockPlaceEvent) {
            }
        });
    }

    public static @NotNull ItemStack createStar(@NotNull Stored stored) {
        ItemStack clone = new CustomItemStack(stored.getItemStack().clone(), name, lore);
        writePDCToStar(clone, stored);
        updateLoreForStar(clone);

        return clone;
    }

    public static void writePDCToStar(@NotNull ItemStack itemStack, @NotNull Stored stored) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(Constants.IDENTIFIER_KEY, PersistentDataType.STRING, stored.getIdentifier());
        Slimefun.getItemDataService().setItemData(meta, instance.getId());
        itemStack.setItemMeta(meta);
    }

    public static @Nullable Stored getStoredFromStar(@NotNull ItemStack star) {
        ItemMeta meta = star.getItemMeta();
        String identifier = meta.getPersistentDataContainer().getOrDefault(Constants.IDENTIFIER_KEY, PersistentDataType.STRING, "");
        Stored stored;
        stored = VanillaStored.loadFromIdentifier(identifier);
        if (stored == null) {
            stored = SlimefunStored.loadFromIdentifier(identifier);
        }

        return stored;
    }

    public static void updateLoreForStar(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            return;
        }

        if (lore.size() < 2) {
            return;
        }

        lore.set(1, ChatColor.translateAlternateColorCodes('&', "&6已存储: " + getStoredFromStar(itemStack).getName()));

        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }
}
