package net.touruya.infiniteblock.utils;

import lombok.experimental.UtilityClass;
import net.touruya.infiniteblock.implementation.InfiniteBlocks;

import java.io.File;

@UtilityClass
public class Constants {
    public static final String PERMISSION_ADMIN = "infiniteblock.admin";
    public static final String PERMISSION_COMMAND_ADMIN = "infiniteblock.command.admin";
    public static final File DATA_FILE = new File(InfiniteBlocks.getInstance().getDataFolder(), "data/data.yml");
}
