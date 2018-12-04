package net.azisaba.yukielevator.config;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfigSound {

    private final ConfigurationSection section;

    @Getter(lazy = true)
    private final Sound type = Sound.valueOf(section.getString("type"));
    @Getter(lazy = true)
    private final float volume = (float) section.getDouble("volume");
    @Getter(lazy = true)
    private final float pitch = (float) section.getDouble("pitch");
}
