package net.azisaba.yukielevator.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.configuration.serialization.ConfigurationSerializable;
import org.simpleyaml.configuration.serialization.ConfigurationSerialization;

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
    public static ElevatorConfig load() {
        ConfigurationSerialization.registerClass( ElevatorConfig.class );

        YamlFile file = new YamlFile( "elevator.yml" );
        if ( !file.exists() ) {
            file.createNewFile( true );
        }
        file.load();
        return (ElevatorConfig) file.get( "" );
    }
}
