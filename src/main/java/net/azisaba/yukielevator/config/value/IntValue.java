package net.azisaba.yukielevator.config.value;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import lombok.Data;

@Data
public class IntValue implements IntSupplier, IntConsumer {

    private final Plugin plugin;
    private final ConfigurationSection section;
    private final String path;

    @Override
    public int getAsInt() {
        return section.getInt(path);
    }

    @Override
    public void accept(int value) {
        section.set(path, value);
    }
}
