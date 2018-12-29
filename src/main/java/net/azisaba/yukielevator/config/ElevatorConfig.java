package net.azisaba.yukielevator.config;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import lombok.Getter;
import lombok.SneakyThrows;

public class ElevatorConfig implements ConfigurationSerializable {

    @Getter
    private Material baseBlockType;
    @Getter
    private int elevatorHeight;

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> mappedObject = new LinkedHashMap<>();
        mappedObject.put( "baseBlockType", baseBlockType );
        mappedObject.put( "elevatorHeight", elevatorHeight );
        return mappedObject;
    }

    public static ElevatorConfig deserialize( Map<String, Object> mappedObject ) {
        ElevatorConfig object = new ElevatorConfig();
        object.baseBlockType = Material.getMaterial( (String) mappedObject.get( "baseBlockType" ) );
        object.elevatorHeight = (int) mappedObject.get( "elevatorHeight" );
        return object;
    }

    @SneakyThrows
    public static ElevatorConfig load( InputStream resource, Path file ) {
        ConfigurationSerialization.registerClass( ElevatorConfig.class );

        if ( !Files.isRegularFile( file ) ) {
            Files.createDirectories( file.getParent() );
            Files.copy( resource, file );
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration( file.toFile() );
        return (ElevatorConfig) config.get( "settings" );
    }
}
