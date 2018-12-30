package net.azisaba.yukielevator.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Data;
import lombok.SneakyThrows;

import net.azisaba.yukielevator.config.obj.Settings;

@Data
public class ElevatorConfig {

	private final InputStream resource;
	private final Path file;

	private YamlConfiguration config;

	private Settings settings;

	@SneakyThrows({ IOException.class })
	public void saveDefaultConfig() {
		if (!Files.isRegularFile(file)) {
			Files.createDirectories(file.getParent());
			Files.copy(resource, file);
		}
	}

	public void loadConfig() {
		this.config = YamlConfiguration.loadConfiguration(file.toFile());
		this.settings = (Settings) config.get("settings");
	}
}
