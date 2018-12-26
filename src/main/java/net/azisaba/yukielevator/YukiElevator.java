package net.azisaba.yukielevator;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

import net.azisaba.yukielevator.config.PluginConfig;
import net.azisaba.yukielevator.listener.ElevatorDownListener;
import net.azisaba.yukielevator.listener.ElevatorUpListener;
import net.azisaba.yukielevator.util.ElevatorSystem;

public class YukiElevator extends JavaPlugin {

    @Getter
    private PluginConfig pluginConfig;
    @Getter
    private ElevatorSystem system;

    @Override
    public void onEnable() {
        this.pluginConfig = new PluginConfig( this );
        pluginConfig.saveDefaultConfig();
        pluginConfig.loadConfig();

        this.system = new ElevatorSystem( pluginConfig.getBaseBlockType(), pluginConfig.getElevatorHeight() );

        getServer().getPluginManager().registerEvents( new ElevatorUpListener( this ), this );
        getServer().getPluginManager().registerEvents( new ElevatorDownListener( this ), this );
    }
}
