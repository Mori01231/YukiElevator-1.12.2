package net.azisaba.yukielevator.config.value;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import lombok.Data;

@Data
public class MaterialValue implements Supplier<Material>, Consumer<Material> {

    private final Plugin plugin;
    private final ConfigurationSection section;
    private final String path;

    @Override
    public Material get() {
        return Material.getMaterial(section.getString(path));
    }

    @Override
    public void accept(Material value) {
        section.set(path, value);
    }
}
