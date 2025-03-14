package net.touruya.infiniteblock.api.stored;

import lombok.Getter;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class VanillaStored implements Stored {
    private final Material material;

    public VanillaStored(Material material) {
        this.material = material;
    }

    public static @Nullable VanillaStored loadFromIdentifier(@NotNull String identifier) {
        String[] parts = identifier.split(":");
        if (parts.length == 2 && parts[0].equals("vanilla")) {
            Material material = Material.getMaterial(parts[1]);
            if (material != null) {
                return new VanillaStored(material);
            }
        }
        return null;
    }

    @Override
    public @NotNull String getName() {
        return ItemStackHelper.getDisplayName(new ItemStack(material));
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        return new ItemStack(material);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "vanilla:" + material.name();
    }
}
