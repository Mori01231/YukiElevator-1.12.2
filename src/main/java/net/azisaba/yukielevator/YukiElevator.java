package net.azisaba.yukielevator;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

public class YukiElevator extends JavaPlugin implements Listener {

    public static final Material BASE_BLOCK_TYPE = Material.DIAMOND_BLOCK;
    public static final int ELEVATOR_HEIGHT = 3;

    public boolean isPlayerJumping( Player player, Location moveFrom, Location moveTo ) {
        return !player.isDead() && !player.isOnGround() && !player.isFlying() && moveFrom.getY() < moveTo.getY() && player.getVelocity().getY() > 0;
    }

    public boolean isSafe( Block block ) {
        return block.getType().isTransparent();
    }

    public boolean isFloor( Block base ) {
        return base.getType() == BASE_BLOCK_TYPE && IntStream.range( 1, ELEVATOR_HEIGHT ).mapToObj( i -> base.getRelative( BlockFace.UP, i ) ).allMatch( this::isSafe );
    }

    public Optional<Block> findNextFloor( Block baseFrom, BlockFace face ) {
        Vector direction = new Vector( face.getModX(), face.getModY(), face.getModZ() );
        Location loc = baseFrom.getLocation().setDirection( direction );
        int maxDistance = baseFrom.getWorld().getMaxHeight();

        BlockIterator it = new BlockIterator( loc, 0, maxDistance );
        Iterators.advance( it, ELEVATOR_HEIGHT );

        List<Block> list = Lists.newArrayList( it );
        int limit = Iterables.indexOf( list, Predicates.and( Predicates.not( this::isFloor ), Predicates.not( this::isSafe ) ) );
        return Iterables.tryFind( Iterables.limit( list, limit == -1 ? list.size() : limit ), this::isFloor ).toJavaUtil();
    }

    public void teleportToFloor( Player player, Block baseFrom, Block baseTo ) {
        Location from = player.getLocation();
        Location to = baseTo.getRelative( BlockFace.UP ).getLocation();

        Vector relativeXZ = from.subtract( baseFrom.getLocation() ).toVector().setY( 0 );
        to.add( relativeXZ );

        Vector direction = from.getDirection();
        to.setDirection( direction );

        player.teleport( to );
        player.playSound( to, Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1 );
        player.getWorld().spawnParticle( Particle.TOTEM, to, 50, 0.2, 0.2, 0.2, 0.5 );
    }

    @EventHandler
    public void onElevatorUp( PlayerMoveEvent event ) {
        Player player = event.getPlayer();
        if ( !player.hasPermission( "yukielevator.use" ) && !player.hasPermission( "yukielevator.up" ) ) {
            return;
        }

        Block baseFrom = player.getLocation().getBlock().getRelative( BlockFace.DOWN );
        if ( isFloor( baseFrom ) ) {
            if ( isPlayerJumping( player, event.getFrom(), event.getTo() ) ) {
                findNextFloor( baseFrom, BlockFace.UP ).ifPresent( baseTo -> teleportToFloor( player, baseFrom, baseTo ) );
            }
        }
    }

    @EventHandler
    public void onElevatorDown( PlayerToggleSneakEvent event ) {
        Player player = event.getPlayer();
        if ( !player.hasPermission( "yukielevator.use" ) && !player.hasPermission( "yukielevator.down" ) ) {
            return;
        }

        Block baseFrom = player.getLocation().getBlock().getRelative( BlockFace.DOWN );
        if ( isFloor( baseFrom ) ) {
            if ( event.isSneaking() ) {
                findNextFloor( baseFrom, BlockFace.DOWN ).ifPresent( baseTo -> teleportToFloor( player, baseFrom, baseTo ) );
            }
        }
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents( this, this );
    }
}
