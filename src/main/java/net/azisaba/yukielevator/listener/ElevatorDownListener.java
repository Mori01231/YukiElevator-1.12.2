package net.azisaba.yukielevator.listener;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import lombok.RequiredArgsConstructor;

import net.azisaba.yukielevator.YukiElevator;

import me.rayzr522.jsonmessage.JSONMessage;

@RequiredArgsConstructor
public class ElevatorDownListener implements Listener {

    private final YukiElevator plugin;

    @EventHandler
    public void onElevatorDown( PlayerToggleSneakEvent event ) {
        Player player = event.getPlayer();

        Block baseFrom = player.getLocation().getBlock().getRelative( BlockFace.DOWN );
        if ( !plugin.getSystem().isFloor( baseFrom ) ) {
            return;
        }
        if ( !event.isSneaking() ) {
            return;
        }

        plugin.getSystem().tryFindFloor( baseFrom, BlockFace.DOWN ).ifPresent( baseTo -> {
            if ( !player.hasPermission( "yukielevator.use" ) && !player.hasPermission( "yukielevator.down" ) ) {
                JSONMessage.create( ChatColor.RED + "あなたはエレベーターを下る権限を持っていません！" ).tooltip( "yukielevator.use または yukielevator.down のどちらかの権限が必要です。" ).send( player );
                return;
            }

            plugin.getSystem().teleportToFloor( player, baseFrom, baseTo );
        } );
    }
}
