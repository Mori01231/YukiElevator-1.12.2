package net.azisaba.yukielevator.config;

import org.bukkit.Material;

import lombok.Getter;

import net.azisaba.yukielevator.YukiElevator;

public class PluginConfig extends AbstractConfig {

	@Getter
	private Material baseBlockType;
	@Getter
	private int elevatorHeight;

	public PluginConfig(YukiElevator plugin) {
		super(plugin, "config.yml", "./config.yml");
	}

	@Override
	protected void loadValues() {
		this.baseBlockType = Material.getMaterial(config.getString("baseBlockType", "DIAMOND_BLOCK"));
		this.elevatorHeight = config.getInt("elevatorHeight", 3);
	}

	@Override
	protected void saveValues() {
		config.set("baseBlockType", baseBlockType);
		config.set("elevatorHeight", elevatorHeight);
	}
}
