package net.azisaba.yukielevator.command;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import lombok.RequiredArgsConstructor;

import net.azisaba.yukielevator.YukiElevator;

@RequiredArgsConstructor
public class YukiElevatorCommand implements CommandExecutor, TabCompleter {

    private final YukiElevator plugin;

    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
        if ( args.length >= 1 && args[0].equalsIgnoreCase( "reload" ) ) {
            plugin.getPluginConfig().reloadConfig();
            sender.sendMessage( "設定ファイルを再読み込みしました。" );
            return true;
        }

        if ( args.length >= 2 && args[0].equalsIgnoreCase( "settype" ) ) {
            Material type = Material.getMaterial( args[1] );
            if ( type == null ) {
                sender.sendMessage( ChatColor.RED + args[1] + " は無効なブロックの種類です。" );
                return true;
            }

            plugin.getPluginConfig().setBaseBlockType( type );
            sender.sendMessage( "エレベーターのベースブロックを " + type + " に設定しました。" );
            return true;
        }

        sender.sendMessage( ChatColor.RED + "使用法: /" + label + " reload OR /" + label + " settype <ブロックの種類>" );
        return false;
    }

    @Override
    public List<String> onTabComplete( CommandSender sender, Command command, String alias, String[] args ) {
        if ( args.length == 1 ) {
            return Stream.of( "reload", "settype" )
                    .filter( str -> str.startsWith( args[0] ) )
                    .collect( Collectors.toList() );
        }
        if ( args.length == 2 && args[0].equalsIgnoreCase( "settype" ) ) {
            return Stream.of( Material.values() )
                    .filter( Material::isBlock )
                    .map( Material::toString )
                    .filter( str -> str.startsWith( args[1] ) )
                    .collect( Collectors.toList() );
        }
        return null;
    }
}
