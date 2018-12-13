package net.azisaba.yukielevator;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

import net.azisaba.yukielevator.command.YukiElevatorCommand;
import net.azisaba.yukielevator.config.PluginConfig;
import net.azisaba.yukielevator.listener.ElevatorListener;

public class YukiElevator extends JavaPlugin {

    @Getter
    private final PluginConfig pluginConfig = new PluginConfig( this );

    @Override
    public void onEnable() {
        pluginConfig.loadConfig();

        getCommand( "yukielevator" ).setExecutor( new YukiElevatorCommand( this ) );
        getCommand( "yukielevator" ).setTabCompleter( new YukiElevatorCommand( this ) );
        getServer().getPluginManager().registerEvents( new ElevatorListener( this ), this );
    }

    @Override
    public void onDisable() {
        pluginConfig.saveConfig();
    }
}
