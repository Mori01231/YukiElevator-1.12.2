package net.azisaba.yukielevator;

import org.bukkit.plugin.java.JavaPlugin;

import net.azisaba.yukielevator.listener.ElevatorDownListener;
import net.azisaba.yukielevator.listener.ElevatorUpListener;

public class YukiElevator extends JavaPlugin {

    private ElevatorConfig elevatorConfig;
    private ElevatorSystem system;

    public ElevatorConfig getElevatorConfig() {
        return elevatorConfig;
    }

    public ElevatorSystem getSystem() {
        return system;
    }

    @Override
    public void onEnable() {
        this.elevatorConfig = new ElevatorConfig(this);
        elevatorConfig.saveDefaultConfig();
        elevatorConfig.loadConfig();

        this.system = new ElevatorSystem(elevatorConfig);

        getServer().getPluginManager().registerEvents(new ElevatorUpListener(this), this);
        getServer().getPluginManager().registerEvents(new ElevatorDownListener(this), this);
    }
}
