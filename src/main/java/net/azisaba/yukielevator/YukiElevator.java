package net.azisaba.yukielevator;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

import net.azisaba.yukielevator.config.ElevatorConfig;
import net.azisaba.yukielevator.listener.ElevatorDownListener;
import net.azisaba.yukielevator.listener.ElevatorUpListener;
import net.azisaba.yukielevator.util.ElevatorSystem;

public class YukiElevator extends JavaPlugin {

    @Getter
    private ElevatorConfig elevatorConfig;
    @Getter
    private ElevatorSystem system;

    @Override
    public void onEnable() {
        this.elevatorConfig = new ElevatorConfig( this );
        elevatorConfig.saveDefaultConfig();
        elevatorConfig.loadConfig();

        this.system = new ElevatorSystem( elevatorConfig.getBaseBlockType(), elevatorConfig.getElevatorHeight() );

        getServer().getPluginManager().registerEvents( new ElevatorUpListener( this ), this );
        getServer().getPluginManager().registerEvents( new ElevatorDownListener( this ), this );
    }
}
