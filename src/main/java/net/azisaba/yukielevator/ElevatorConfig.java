package net.azisaba.yukielevator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.primitives.Ints;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

public class ElevatorConfig {

    private final YukiElevator plugin;
    private final InputStream resource;
    private final File file;

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
        this.file = new File(plugin.getDataFolder(), "elevator.yml");
    }

    public void saveDefaultConfig() {
        if (file.isFile()) {
            return;
        }
        try {
            Files.createParentDirs(file);
            Files.asByteSink(file).writeFrom(resource);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "デフォルトの設定ファイルの保存中にエラーが発生しました。", ex);
        }
    }

    public void loadConfig() {
        this.config = YamlConfiguration.loadConfiguration(file);
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
            Map<String, Object> values = Maps.newLinkedHashMap();
            values.put("baseBlockType", baseBlockType);
            values.put("elevatorHeight", elevatorHeight);
            return values;
        }

        public static Settings deserialize(Map<String, Object> values) {
            Settings obj = new Settings();
            obj.baseBlockType = Material.getMaterial((String) values.get("baseBlockType"));
            obj.elevatorHeight = Ints.tryParse((String) values.get("elevatorHeight"));
            return obj;
        }
    }
}
