package net.azisaba.yukielevator;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

import net.azisaba.yukielevator.config.ElevatorConfig;
import net.azisaba.yukielevator.config.obj.Settings;
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
		ConfigurationSerialization.registerClass(Settings.class, "YukiElevator.Settings");

		this.elevatorConfig = new ElevatorConfig(getResource("elevator.yml"), getDataFolder().toPath().resolve("elevator.yml"));
		elevatorConfig.saveDefaultConfig();
		elevatorConfig.loadConfig();

		this.system = new ElevatorSystem(elevatorConfig.getSettings().getBaseBlockType(), elevatorConfig.getSettings().getElevatorHeight());

		getServer().getPluginManager().registerEvents(new ElevatorUpListener(this), this);
		getServer().getPluginManager().registerEvents(new ElevatorDownListener(this), this);
	}
}
