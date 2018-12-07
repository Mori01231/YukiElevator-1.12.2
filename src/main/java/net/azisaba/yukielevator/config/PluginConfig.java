package net.azisaba.yukielevator.config;

import lombok.Data;
import lombok.Getter;

import net.azisaba.yukielevator.YukiElevator;
import net.azisaba.yukielevator.config.value.IntValue;
import net.azisaba.yukielevator.config.value.MaterialValue;

@Data
public class PluginConfig {

    private final YukiElevator plugin;

    @Getter(lazy = true)
    private final MaterialValue baseBlockType = new MaterialValue(plugin, plugin.getConfig(), "baseBlockType");
    @Getter(lazy = true)
    private final IntValue elevatorHeight = new IntValue(plugin, plugin.getConfig(), "elevatorHeight");
}
