package net.azisaba.yukielevator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

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
}
