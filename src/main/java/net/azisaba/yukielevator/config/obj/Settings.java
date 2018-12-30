package net.azisaba.yukielevator.config.obj;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import lombok.Data;

@Data
@SerializableAs("YukiElevator.Settings")
public class Settings implements ConfigurationSerializable {

	private Material baseBlockType;
	private int elevatorHeight;

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> values = new LinkedHashMap<String, Object>();
		values.put("baseBlockType", baseBlockType);
		values.put("elevatorHeight", elevatorHeight);
		return values;
	}

	public static Settings deserialize(Map<String, Object> values) {
		Settings obj = new Settings();
		obj.baseBlockType = Material.getMaterial((String) values.get("baseBlockType"));
		obj.elevatorHeight = (int) values.get("elevatorHeight");
		return obj;
	}
}
