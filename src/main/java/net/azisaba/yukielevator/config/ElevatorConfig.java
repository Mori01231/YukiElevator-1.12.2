package net.azisaba.yukielevator.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import net.azisaba.yukielevator.YukiElevator;

public class ElevatorConfig extends YamlConfiguration {

    private final YukiElevator plugin;
    private final Supplier<InputStream> resourceSupplier;
    private final Path outputFile;

    private Material baseBlockType;
    private int elevatorHeight;

    public ElevatorConfig(YukiElevator plugin) {
        this.plugin = plugin;
        this.resourceSupplier = () -> plugin.getClass().getClassLoader().getResourceAsStream("config/elevator.yml");
        this.outputFile = plugin.getDataFolder().toPath().resolve("elevator.yml");
    }

    public YukiElevator getPlugin() {
        return plugin;
    }

    public Supplier<InputStream> getResourceSupplier() {
        return resourceSupplier;
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
        if (Files.isRegularFile(outputFile)) {
            return;
        }

        try {
            Files.createDirectories(outputFile.getParent());
            Files.copy(resourceSupplier.get(), outputFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "設定ファイルの保存中にエラーが発生しました。", ex);
        }
    }

    public void saveDefaultConfigAsync() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::saveDefaultConfig);
    }

    public void loadConfig() {
        try {
            if (Files.isRegularFile(outputFile)) {
                load(outputFile.toFile());
            } else {
                load(new InputStreamReader(resourceSupplier.get()));
                saveDefaultConfigAsync();
            }
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "設定ファイルの読み込み中にエラーが発生しました。", ex);
        } catch (InvalidConfigurationException ex) {
            plugin.getLogger().log(Level.SEVERE, "設定ファイルの書式が間違っています。", ex);
        }

        this.baseBlockType = Material.getMaterial(getString("baseBlockType", "DIAMOND_BLOCK"));
        this.elevatorHeight = getInt("elevatorHeight", 3);
    }
}
