package net.azisaba.yukielevator.config;

import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfigParticle {

    private final ConfigurationSection section;

    @Getter(lazy = true)
    private final Particle type = Particle.valueOf(section.getString("type"));
    @Getter(lazy = true)
    private final int count = section.getInt("count");
    @Getter(lazy = true)
    private final double offsetX = section.getDouble("offsetX");
    @Getter(lazy = true)
    private final double offsetY = section.getDouble("offsetY");
    @Getter(lazy = true)
    private final double offsetZ = section.getDouble("offsetZ");
    @Getter(lazy = true)
    private final double extra = section.getDouble("extra");
}
