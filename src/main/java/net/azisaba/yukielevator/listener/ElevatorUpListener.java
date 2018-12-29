package net.azisaba.yukielevator.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import lombok.RequiredArgsConstructor;

import net.azisaba.yukielevator.YukiElevator;

import me.rayzr522.jsonmessage.JSONMessage;

@RequiredArgsConstructor
public class ElevatorUpListener implements Listener {

    private final YukiElevator plugin;

    public boolean isPlayerJumping( Player player, Location moveFrom, Location moveTo ) {
        return !player.isDead() && !player.isOnGround() && !player.isFlying() && moveFrom.getY() < moveTo.getY() && player.getVelocity().getY() > 0;
    }

    @EventHandler
    public void onElevatorUp( PlayerMoveEvent event ) {
        Player player = event.getPlayer();

        Block baseFrom = player.getLocation().getBlock().getRelative( BlockFace.DOWN );
        if ( !plugin.getSystem().isFloor( baseFrom ) ) {
            return;
        }
        if ( !isPlayerJumping( player, event.getFrom(), event.getTo() ) ) {
            return;
        }

        plugin.getSystem().tryFindFloor( baseFrom, BlockFace.UP ).ifPresent( baseTo -> {
            if ( !player.hasPermission( "yukielevator.use" ) && !player.hasPermission( "yukielevator.up" ) ) {
                JSONMessage.create( ChatColor.RED + "あなたはエレベーターを上る権限を持っていません！" ).tooltip( "yukielevator.use または yukielevator.up のどちらかの権限が必要です。" ).send( player );
                return;
            }

            plugin.getSystem().teleportToFloor( player, baseFrom, baseTo );
        } );
    }
}
