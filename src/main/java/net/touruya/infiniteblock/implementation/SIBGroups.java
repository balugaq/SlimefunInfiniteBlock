package net.touruya.infiniteblock.implementation;

import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import lombok.experimental.UtilityClass;
import net.touruya.infiniteblock.utils.Constants;
import net.touruya.infiniteblock.utils.Icons;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class SIBGroups {
    public static NestedItemGroup NESTED_GROUP;
    public static SubItemGroup MACHINES_GROUP;
    public static SubItemGroup MATERIALS_GROUP;

    public static void setup(@NotNull InfiniteBlocks plugin) {
        NESTED_GROUP = new NestedItemGroup(
                Constants.INFINITE_GROUP,
                Icons.NESTED_GROUP
        );

        // 创建机器子组
        MACHINES_GROUP = new SubItemGroup(
                Constants.MACHINES,
                NESTED_GROUP,
                Icons.MACHINES_GROUP
        );

        // 创建材料子组
        MATERIALS_GROUP = new SubItemGroup(
                Constants.MATERIALS,
                NESTED_GROUP,
                Icons.MATERIALS_GROUP
        );

        NESTED_GROUP.register(plugin);
        MACHINES_GROUP.register(plugin);
        MATERIALS_GROUP.register(plugin);
    }
}
