package net.azisaba.yukielevator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

public class ElevatorConfig {

    private final YukiElevator plugin;
    private final InputStream resource;
    private final Path file;

    private YamlConfiguration config;
    private Settings settings;

    public YamlConfiguration getConfig() {
        return config;
    }

    public Settings getSettings() {
        return settings;
    }

    public ElevatorConfig(YukiElevator plugin) {
        this.plugin = plugin;
        this.resource = plugin.getResource("elevator.yml");
        this.file = plugin.getDataFolder().toPath().resolve("elevator.yml");
    }

    public void saveDefaultConfig() {
        if (Files.isRegularFile(file)) {
            return;
        }
        try {
            Files.createDirectories(file.getParent());
            Files.copy(resource, file);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "デフォルトの設定ファイルの保存中にエラーが発生しました。", ex);
        }
    }

    public void loadConfig() {
        this.config = YamlConfiguration.loadConfiguration(file.toFile());
        this.settings = (Settings) config.get("settings");
    }

    @SerializableAs("ElevatorConfig.Settings")
    public static class Settings implements ConfigurationSerializable {

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
            obj.baseBlockType = Material.getMaterial((String) values.get("baseBlockType"));
            obj.elevatorHeight = (int) values.get("elevatorHeight");
            return obj;
        }
    }
}
