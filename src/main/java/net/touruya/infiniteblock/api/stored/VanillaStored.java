package net.touruya.infiniteblock.api.stored;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VanillaStored implements Stored {
    private final Material material;
    public VanillaStored(Material material) {
        this.material = material;
    }

    @Override
    public String getName() {
        return material.name();
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(material);
    }

    @Override
    public String getIdentifier() {
        return "vanilla:" + material.name();
    }

    public static VanillaStored loadFromIdentifier(String identifier) {
        String[] parts = identifier.split(":");
        if (parts.length == 2 && parts[0].equals("vanilla")) {
            Material material = Material.getMaterial(parts[1]);
            if (material != null) {
                return new VanillaStored(material);
            }
        }
        return null;
    }
}
