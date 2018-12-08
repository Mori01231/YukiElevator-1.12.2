package net.azisaba.yukielevator.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import lombok.Data;
import lombok.Getter;

import net.azisaba.yukielevator.YukiElevator;

@Data
public class PluginConfig {

    private final YukiElevator plugin;

    @Getter(lazy = true)
    private final FileConfiguration config = plugin.getConfig();

    public Material getBaseBlockType() {
        return Material.getMaterial(getConfig().getString("baseBlockType"));
    }

    public int getElevatorHeight() {
        return getConfig().getInt("elevatorHeight");
    }
}
