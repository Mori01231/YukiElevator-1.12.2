package net.azisaba.yukielevator.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import net.azisaba.yukielevator.YukiElevator;

public class ElevatorConfig extends YamlConfiguration {

    private final YukiElevator plugin;
    private final InputStream resourceFile;
    private final Path outputFile;

    private Material baseBlockType;
    private int elevatorHeight;

    public ElevatorConfig(YukiElevator plugin) {
        this.plugin = plugin;
        this.resourceFile = plugin.getResource("config/elevator.yml");
        this.outputFile = plugin.getDataFolder().toPath().resolve("elevator.yml");
    }

    public YukiElevator getPlugin() {
        return plugin;
    }

    public InputStream getResourceFile() {
        return resourceFile;
    }

    public Path getOutputFile() {
        return outputFile;
    }

    public Material getBaseBlockType() {
        return baseBlockType;
    }

    public int getElevatorHeight() {
        return elevatorHeight;
    }

    public void saveDefaultConfig() {
        if (Files.isRegularFile(outputFile))
            return;

        try {
            Files.createDirectories(outputFile.getParent());
            Files.copy(resourceFile, outputFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "設定ファイルの保存中にエラーが発生しました。", ex);
        }
    }

    public void saveDefaultConfigAsync() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::saveDefaultConfig);
    }

    public void loadConfig() {
        Reader reader = null;

        if (Files.isRegularFile(outputFile)) {
            try {
                reader = Files.newBufferedReader(outputFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "設定ファイルを読み込み用として開けません。", ex);
            }
        } else {
            reader = new BufferedReader(new InputStreamReader(resourceFile));

            saveDefaultConfigAsync();
        }

        if (reader != null)
            try {
                load(reader);
            } catch (IOException | InvalidConfigurationException ex) {
                plugin.getLogger().log(Level.SEVERE, "設定ファイルの読み込み中にエラーが発生しました。", ex);
            }

        this.baseBlockType = Material.getMaterial(getString("baseBlockType", "DIAMOND_BLOCK"));
        this.elevatorHeight = getInt("elevatorHeight", 3);
    }
}
