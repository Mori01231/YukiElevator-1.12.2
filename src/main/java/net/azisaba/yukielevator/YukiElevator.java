package net.azisaba.yukielevator;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

import net.azisaba.yukielevator.config.PluginConfig;
import net.azisaba.yukielevator.listener.ElevatorListener;

public class YukiElevator extends JavaPlugin {

	@Getter
	private final PluginConfig pluginConfig = new PluginConfig(this);

	@Override
	public void onLoad() {
		pluginConfig.loadConfig();
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new ElevatorListener(this), this);
	}
}
