package net.azisaba.yukielevator.config;

import org.bukkit.Material;

import lombok.Data;
import lombok.EqualsAndHashCode;

import net.azisaba.yukielevator.YukiElevator;

import io.github.yukileafx.yukiconfig.bukkit.BukkitYamlConfig;

@Data
@EqualsAndHashCode( callSuper = true )
public class PluginConfig extends BukkitYamlConfig {

    private Material baseBlockType;
    private int elevatorHeight;

    public PluginConfig( YukiElevator plugin ) {
        super( plugin, "config.yml" );
    }

    @Override
    protected void loadValues() {
        this.baseBlockType = Material.getMaterial( getConfig().getString( "baseBlockType", "DIAMOND_BLOCK" ) );
        this.elevatorHeight = getConfig().getInt( "elevatorHeight", 3 );
    }

    @Override
    protected void saveValues() {
        getConfig().set( "baseBlockType", baseBlockType.toString() );
        getConfig().set( "elevatorHeight", elevatorHeight );
    }
}
