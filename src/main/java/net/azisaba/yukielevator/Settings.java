package net.azisaba.yukielevator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("yukielevator.Settings")
public class Settings implements ConfigurationSerializable {

    private Material baseBlockType;
    private int elevatorHeight;

    public Material getBaseBlockType() {
        return baseBlockType;
    }

    public void setBaseBlockType(Material baseBlockType) {
        this.baseBlockType = baseBlockType;
    }

    public int getElevatorHeight() {
        return elevatorHeight;
    }

    public void setElevatorHeight(int elevatorHeight) {
        this.elevatorHeight = elevatorHeight;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("baseBlockType", baseBlockType);
        values.put("elevatorHeight", elevatorHeight);
        return values;
    }

    public static Settings deserialize(Map<String, Object> values) {
        Settings obj = new Settings();

        String baseBlockType = (String) values.getOrDefault("baseBlockType", "DIAMOND_BLOCK");
        obj.baseBlockType = Material.getMaterial(baseBlockType);

        int elevatorHeight = (int) values.getOrDefault("elevatorHeight", 3);
        obj.elevatorHeight = elevatorHeight;

        return obj;
    }
}
