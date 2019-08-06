package net.azisaba.yukielevator.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Config extends YamlConfiguration {

    private final Plugin plugin;
    private final Supplier<InputStream> resourceSupplier;
    private final Path outputFile;

    public Config(Plugin plugin, Supplier<InputStream> resourceSupplier, Path outputFile) {
        this.plugin = plugin;
        this.resourceSupplier = resourceSupplier;
        this.outputFile = outputFile;
    }

    public Config(Plugin plugin, String resourcePath, String outputPath) {
        this.plugin = plugin;
        resourceSupplier = () -> plugin.getClass().getClassLoader().getResourceAsStream(resourcePath);
        outputFile = plugin.getDataFolder().toPath().resolve(outputPath);
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Supplier<InputStream> getResourceSupplier() {
        return resourceSupplier;
    }

    public Path getOutputFile() {
        return outputFile;
    }

    public void saveDefaultConfig() {
        try {
            if ( !Files.isRegularFile(outputFile) ) {
                Files.createDirectories(outputFile.getParent());
                Files.copy(resourceSupplier.get(), outputFile);
            }
        } catch ( IOException ex ) {
            plugin.getLogger().log(Level.SEVERE, "設定ファイルの保存中にエラーが発生しました。", ex);
        }
    }

    public void saveDefaultConfigAsync() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> saveDefaultConfig());
    }

    public void onLoad() {
    }

    public void onSave() {
    }

    public void loadConfig() {
        try {
            if ( Files.isRegularFile(outputFile) ) {
                load(outputFile.toFile());
            } else {
                load(new InputStreamReader(resourceSupplier.get()));
                saveDefaultConfigAsync();
            }
        } catch ( IOException ex ) {
            plugin.getLogger().log(Level.SEVERE, "設定ファイルの読み込み中にエラーが発生しました。", ex);
        } catch ( InvalidConfigurationException ex ) {
            plugin.getLogger().log(Level.SEVERE, "設定ファイルの書式が間違っています。", ex);
        }
        onLoad();
    }

    public void saveConfig() {
        onSave();
        try {
            save(outputFile.toFile());
        } catch ( IOException ex ) {
            plugin.getLogger().log(Level.SEVERE, "設定ファイルの保存中にエラーが発生しました。", ex);
        }
    }
}
