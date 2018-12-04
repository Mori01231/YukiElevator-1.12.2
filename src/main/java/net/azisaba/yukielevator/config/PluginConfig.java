package net.azisaba.yukielevator.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import net.azisaba.yukielevator.YukiElevator;

@RequiredArgsConstructor
public class PluginConfig {

    private final YukiElevator plugin;
    @Getter(lazy = true)
    private final FileConfiguration config = plugin.getConfig();

    @Getter(lazy = true)
    private final Material baseBlockType = Material.getMaterial(getConfig().getString("baseBlockType"));
    @Getter(lazy = true)
    private final double baseBlockHeight = getConfig().getDouble("baseBlockHeight");
    @Getter(lazy = true)
    private final ConfigSound soundOnUp = new ConfigSound(getConfig().getConfigurationSection("soundOnUp"));
    @Getter(lazy = true)
    private final ConfigParticle particleOnUp = new ConfigParticle(getConfig().getConfigurationSection("particleOnUp"));
    @Getter(lazy = true)
    private final ConfigSound soundOnDown = new ConfigSound(getConfig().getConfigurationSection("soundOnDown"));
    @Getter(lazy = true)
    private final ConfigParticle particleOnDown = new ConfigParticle(getConfig().getConfigurationSection("particleOnDown"));
}
