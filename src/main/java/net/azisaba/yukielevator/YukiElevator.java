package net.azisaba.yukielevator;

import org.bukkit.plugin.java.JavaPlugin;

import net.azisaba.yukielevator.config.ElevatorConfig;
import net.azisaba.yukielevator.elevator.ElevatorSystem;
import net.azisaba.yukielevator.listener.ElevatorDownListener;
import net.azisaba.yukielevator.listener.ElevatorUpListener;

public class YukiElevator extends JavaPlugin {

	private ElevatorConfig config;
	private ElevatorSystem system;

	@Override
	public ElevatorConfig getConfig() {
		return config;
	}

	public ElevatorSystem getSystem() {
		return system;
	}

	@Override
	public void onEnable() {
		this.config = new ElevatorConfig(this);
		config.loadConfig();

		this.system = new ElevatorSystem(config);

		getServer().getPluginManager().registerEvents(new ElevatorUpListener(this), this);
		getServer().getPluginManager().registerEvents(new ElevatorDownListener(this), this);
	}
}
