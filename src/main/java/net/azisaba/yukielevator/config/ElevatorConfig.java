package net.azisaba.yukielevator.config;

import org.bukkit.Material;

import net.azisaba.yukielevator.YukiElevator;

public class ElevatorConfig extends Config {

    private Material baseBlockType;
    private int elevatorHeight;

    public ElevatorConfig(YukiElevator plugin) {
        super(plugin, "config/elevator.yml", "elevator.yml");
    }

    public Material getBaseBlockType() {
        return baseBlockType;
    }

    public int getElevatorHeight() {
        return elevatorHeight;
    }

    @Override
    public void onLoad() {
        baseBlockType = Material.getMaterial(getString("baseBlockType", "DIAMOND_BLOCK"));
        elevatorHeight = getInt("elevatorHeight", 3);
    }
}
