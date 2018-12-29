package net.azisaba.yukielevator;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

import net.azisaba.yukielevator.config.ElevatorConfig;
import net.azisaba.yukielevator.listener.ElevatorDownListener;
import net.azisaba.yukielevator.listener.ElevatorUpListener;
import net.azisaba.yukielevator.util.ElevatorSystem;

public class YukiElevator extends JavaPlugin {

    @Getter
    private final ElevatorConfig elevatorConfig = ElevatorConfig.load( getResource( "elevator.yml" ), new File( getDataFolder(), "elevator.yml" ) );
    @Getter
    private final ElevatorSystem system = new ElevatorSystem( elevatorConfig.getBaseBlockType(), elevatorConfig.getElevatorHeight() );

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents( new ElevatorUpListener( this ), this );
        getServer().getPluginManager().registerEvents( new ElevatorDownListener( this ), this );
    }
}
