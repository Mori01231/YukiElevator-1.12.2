package net.azisaba.yukielevator.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import net.azisaba.yukielevator.YukiElevator;

public class ElevatorConfig extends YamlConfiguration {

    private final YukiElevator plugin;
    private final URL resourceFile;
    private final Path outputFile;

    private Material baseBlockType;
    private int elevatorHeight;

    public ElevatorConfig(YukiElevator plugin) {
        this.plugin = plugin;
        this.resourceFile = plugin.getClass().getClassLoader().getResource("config/elevator.yml");
        this.outputFile = plugin.getDataFolder().toPath().resolve("elevator.yml");
    }

    public YukiElevator getPlugin() {
        return plugin;
    }

    public URL getResourceFile() {
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

    public InputStream openResourceFileStream() throws IOException {
        URLConnection conn = resourceFile.openConnection();
        conn.setUseCaches(false);
        return conn.getInputStream();
    }

    public void saveDefaultConfig() {
        if (Files.isRegularFile(outputFile))
            return;

        try {
            Files.createDirectories(outputFile.getParent());

            InputStream in = openResourceFileStream();
            Files.copy(in, outputFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "設定ファイルの保存中にエラーが発生しました。", ex);
        }
    }

    public void saveDefaultConfigAsync() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::saveDefaultConfig);
    }

    public void loadConfig() {
        Reader reader = null;

        try {
            if (Files.isRegularFile(outputFile)) {
                reader = Files.newBufferedReader(outputFile);
            } else {
                InputStream in = openResourceFileStream();
                reader = new BufferedReader(new InputStreamReader(in));

                saveDefaultConfigAsync();
            }
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "設定ファイルを読み込み用として開けません。", ex);
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
