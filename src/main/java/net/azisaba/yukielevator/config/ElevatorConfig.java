package net.azisaba.yukielevator.config;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
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
    public static ElevatorConfig load( InputStream resource, File file ) {
        ConfigurationSerialization.registerClass( ElevatorConfig.class );

        if ( !file.exists() ) {
            Files.copy( resource, file.toPath() );
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration( file );
        return (ElevatorConfig) config.get( "settings" );
    }
}
