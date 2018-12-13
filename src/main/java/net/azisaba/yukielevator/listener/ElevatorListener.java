package net.azisaba.yukielevator.listener;

import java.util.Optional;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import lombok.RequiredArgsConstructor;

import net.azisaba.yukielevator.YukiElevator;

@RequiredArgsConstructor
public class ElevatorListener implements Listener {

    private final YukiElevator plugin;

    private boolean isSafe( Block block ) {
        return block.getType().isTransparent();
    }

    private boolean isFloor( Block base ) {
        Material baseBlockType = plugin.getPluginConfig().getBaseBlockType();
        if ( base.getType() != baseBlockType ) {
            return false;
        }

        int elevatorHeight = plugin.getPluginConfig().getElevatorHeight();
        return IntStream.range( 1, elevatorHeight ).allMatch( up -> isSafe( base.getRelative( BlockFace.UP, up ) ) );
    }

    private boolean isPlayerJumping( Player player, Location moveFrom, Location moveTo ) {
        if ( player.isDead() ) {
            return false;
        }
        if ( player.isOnGround() || player.isFlying() ) {
            return false;
        }

        return moveFrom.getY() < moveTo.getY() && player.getVelocity().getY() > 0;
    }

    private Optional<Block> findNextFloor( Block baseFrom, BlockFace face ) {
        Vector direction = new Vector( face.getModX(), face.getModY(), face.getModZ() );
        Location loc = baseFrom.getLocation().setDirection( direction );
        int maxDistance = baseFrom.getWorld().getMaxHeight();

        BlockIterator it = new BlockIterator( loc, 0, maxDistance );

        int elevatorHeight = plugin.getPluginConfig().getElevatorHeight();
        AtomicBoolean searching = new AtomicBoolean( true );
        return StreamSupport.stream( Spliterators.spliteratorUnknownSize( it, 0 ), false )
                .skip( elevatorHeight )
                .filter( baseTo -> searching.compareAndSet( true, isFloor( baseTo ) || isSafe( baseTo ) ) )
                .filter( this::isFloor )
                .findFirst();
    }

    private void teleportToFloor( Player player, Block baseFrom, Block baseTo ) {
        Location from = player.getLocation();
        Location to = baseTo.getRelative( BlockFace.UP ).getLocation();

        Vector relativeXZ = from.subtract( baseFrom.getLocation() ).toVector().setY( 0 );
        to.add( relativeXZ );

        Vector direction = from.getDirection();
        to.setDirection( direction );

        player.teleport( to );
        player.playSound( to, Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1 );
        player.getWorld().spawnParticle( Particle.TOTEM, to, 50, 1, 1, 1, 0.5 );
    }

    @EventHandler
    public void onElevatorUp( PlayerMoveEvent event ) {
        Player player = event.getPlayer();
        if ( !( player.hasPermission( "yukielevator.use" ) || player.hasPermission( "yukielevator.up" ) ) ) {
            return;
        }

        Block baseFrom = player.getLocation().getBlock().getRelative( BlockFace.DOWN );
        if ( !isFloor( baseFrom ) ) {
            return;
        }
        if ( isPlayerJumping( player, event.getFrom(), event.getTo() ) ) {
            findNextFloor( baseFrom, BlockFace.UP ).ifPresent( baseTo -> teleportToFloor( player, baseFrom, baseTo ) );
        }
    }

    @EventHandler
    public void onElevatorDown( PlayerToggleSneakEvent event ) {
        Player player = event.getPlayer();
        if ( !( player.hasPermission( "yukielevator.use" ) || player.hasPermission( "yukielevator.down" ) ) ) {
            return;
        }

        Block baseFrom = player.getLocation().getBlock().getRelative( BlockFace.DOWN );
        if ( !isFloor( baseFrom ) ) {
            return;
        }
        if ( event.isSneaking() ) {
            findNextFloor( baseFrom, BlockFace.DOWN )
                    .ifPresent( baseTo -> teleportToFloor( player, baseFrom, baseTo ) );
        }
    }
}
