package net.azisaba.yukielevator.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Preconditions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Config {

	private final Plugin plugin;
	private final String resourcePath;
	private final String outFilePath;

	@Getter(lazy = true)
	private final InputStream resource = plugin.getResource(resourcePath);
	@Getter(lazy = true)
	private final Path outFile = plugin.getDataFolder().toPath().resolve(outFilePath);

	protected YamlConfiguration config;

	public void saveDefaultConfig() {
		if (!Files.isRegularFile(getOutFile())) {
			plugin.saveResource(resourcePath, false);
		}
	}

	public void loadConfig() {
		Preconditions.checkArgument(config == null);

		if (Files.isRegularFile(getOutFile())) {
			File file = getOutFile().toFile();
			this.config = YamlConfiguration.loadConfiguration(file);
		} else {
			Reader reader = new InputStreamReader(getResource());
			this.config = YamlConfiguration.loadConfiguration(reader);

			saveDefaultConfig();
		}

		loadValues();
	}

	public void reloadConfig() {
		this.config = null;
		loadConfig();
	}

	public void saveConfig() {
		Preconditions.checkArgument(config != null);

		saveValues();

		try {
			config.save(getOutFile().toFile());
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE, "設定ファイルの保存中にエラーが発生しました。", ex);
		}
	}

	protected void loadValues() {
	}

	protected void saveValues() {
	}
}
