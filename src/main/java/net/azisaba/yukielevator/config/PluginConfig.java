package net.azisaba.yukielevator.config;

import org.bukkit.Material;

import lombok.Getter;

import net.azisaba.yukielevator.YukiElevator;

import io.github.yukileafx.yukiconfig.FileLink;
import io.github.yukileafx.yukiconfig.ReadableConfig;
import io.github.yukileafx.yukiconfig.YukiConfig;
import io.github.yukileafx.yukiconfig.loader.BukkitLoader;

public class PluginConfig extends YukiConfig<BukkitLoader> implements ReadableConfig {

    @Getter
    private Material baseBlockType;
    @Getter
    private int elevatorHeight;

    public PluginConfig( YukiElevator plugin ) {
        super( new BukkitLoader(), FileLink.of( plugin.getClass(), plugin.getDataFolder(), "elevator.yml" ) );
    }

    @Override
    public void onLoad() {
        this.baseBlockType = Material.getMaterial( getLoader().getConfig().getString( "baseBlockType", "DIAMOND_BLOCK" ) );
        this.elevatorHeight = getLoader().getConfig().getInt( "elevatorHeight", 3 );
    }
}
