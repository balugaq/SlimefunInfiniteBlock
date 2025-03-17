package net.touruya.infiniteblock.implementation.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import io.github.thebusybiscuit.slimefun4.libraries.paperlib.PaperLib;
import net.touruya.infiniteblock.api.stored.SlimefunStored;
import net.touruya.infiniteblock.api.stored.Stored;
import net.touruya.infiniteblock.utils.Constants;
import net.touruya.infiniteblock.utils.StoredUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

// todo: 重写createFusion，增加index key，选择交互的pdc
// todo: 左键打开选择菜单
public class FusionBlock extends SlimefunItem {
    private static FusionBlock instance;
    private static @Nullable String name;
    private static String[] lore;

    public FusionBlock(@NotNull ItemGroup itemGroup, @NotNull SlimefunItemStack slimefunItemStack, @NotNull RecipeType recipeType, ItemStack @NotNull [] recipe) {
        super(itemGroup, slimefunItemStack, recipeType, recipe);
        instance = this;
        name = slimefunItemStack.getDisplayName();
        lore = slimefunItemStack.getItemMeta().getLore().toArray(new String[0]);
        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent blockPlaceEvent) {
            }
        });
    }

    public static @NotNull ItemStack createFusion(@NotNull Stored stored, long amount) {
        ItemStack clone = new CustomItemStack(stored.getItemStack().clone(), name, lore);
        writePDCToFusion(clone, stored, amount);
        StoredUtils.updateLoreForFusion(clone);

        return clone;
    }

    public static void writePDCToFusion(@NotNull ItemStack itemStack, @NotNull Stored stored, long amount) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(Constants.STORED_KEY, PersistentDataType.STRING, stored.getIdentifier());
        meta.getPersistentDataContainer().set(Constants.STORED_AMOUNT_KEY, PersistentDataType.LONG, amount);
        Slimefun.getItemDataService().setItemData(meta, instance.getId());
        itemStack.setItemMeta(meta);
    }

    public static void generateParticle(@NotNull Location location) {
        // /particle minecraft:firework ~ ~0.5 ~ 0 0 0 0.1 30
        location.getWorld().spawnParticle(
                Particle.FIREWORKS_SPARK,
                location.clone().add(0.5, 0.5, 0.5),
                10,
                0, 0, 0,
                0.1
        );
    }

    public @Nullable Stored use(@NotNull Player player, @NotNull ItemStack fusion, @NotNull Location location) {
        Stored stored = StoredUtils.getStoredFromFusion(fusion);
        if (stored == null) {
            return null;
        }

        ItemStack storedItem = stored.getItemStack();
        if (!storedItem.getType().isBlock()) {
            return null;
        }

        placeBlock(player, fusion, stored, location);
        StoredUtils.updateLoreForFusion(fusion);
        generateParticle(location);
        return stored;
    }

    public void placeBlock(@NotNull Player player, @NotNull ItemStack fusion, @NotNull Stored stored, @NotNull Location location) {
        final long currentAmount = StoredUtils.getStoredAmountFromFusion(fusion);
        if (currentAmount <= 0) {
            player.sendMessage("融合方块已用完");
            return;
        }

        final Block block = location.getBlock();
        final Material material = stored.getItemStack().getType();
        if (stored instanceof SlimefunStored ss) {
            final SlimefunItem slimefunItem = ss.getItem();
            Slimefun.getDatabaseManager().getBlockDataController().createBlock(location, slimefunItem.getId());
            if (material == Material.PLAYER_HEAD || material == Material.PLAYER_WALL_HEAD) {
                if (slimefunItem.getItem() instanceof SlimefunItemStack sfis) {
                    String texture = sfis.getSkullTexture().orElse(null);
                    if (texture != null) {
                        PlayerSkin skin = PlayerSkin.fromBase64(texture);
                        PlayerHead.setSkin(block, skin, false);
                    }
                }
            }
        }

        long leftAmount = currentAmount;
        if (!StoredUtils.isInfinity(fusion)) {
            leftAmount -= 1;
            writePDCToFusion(fusion, stored, leftAmount);
        }

        if (PaperLib.isPaper()) {
            StoredUtils.updateActionBarForPlayer(player, stored, leftAmount);
        }
    }
}
