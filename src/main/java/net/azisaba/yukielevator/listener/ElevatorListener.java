package net.azisaba.yukielevator.listener;

import java.util.Optional;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.bukkit.Location;
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

import lombok.Data;

import net.azisaba.yukielevator.YukiElevator;

@Data
public class ElevatorListener implements Listener {

    private final YukiElevator plugin;

    private boolean isSafe(Block block) {
        return block.getType().isTransparent();
    }

    private boolean isFloor(Block base) {
        return base.getType() == plugin.getPluginConfig().getBaseBlockType() && IntStream.range(1, plugin.getPluginConfig().getElevatorHeight()) //
                .allMatch(up -> isSafe(base.getRelative(BlockFace.UP, up)));
    }

    private boolean isPlayerJumping(Player player, Location moveFrom, Location moveTo) {
        return !player.isFlying() && !player.isOnGround() && moveFrom.getY() != moveTo.getY() && player.getVelocity().getY() > 0;
    }

    private Optional<Block> findNextFloor(Block from, BlockFace face) {
        Vector direction = new Vector(face.getModX(), face.getModY(), face.getModZ());
        BlockIterator it = new BlockIterator(from.getLocation().setDirection(direction));
        AtomicBoolean searching = new AtomicBoolean(true);
        int size = from.getWorld().getMaxHeight();
        return StreamSupport.stream(Spliterators.spliterator(it, size, 0), false) //
                .filter(base -> searching.compareAndSet(true, isFloor(base) || isSafe(base))) //
                .filter(this::isFloor) //
                .findFirst();
    }

    private void teleportToFloor(Player player, Block base) {
        Location from = player.getLocation();
        Location to = base.getRelative(BlockFace.UP).getLocation().add(0.5, 0, 0.5).setDirection(from.getDirection());
        player.teleport(to);
        player.playSound(to, Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
        player.getWorld().spawnParticle(Particle.TOTEM, to, 50, 1, 1, 1, 0.5, 0);
    }

    @EventHandler
    public void onElevatorUp(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (Stream.of("yukielevator.use", "yukielevator.up").noneMatch(player::hasPermission)) {
            return;
        }

        Block from = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (isFloor(from)) {
            if (isPlayerJumping(player, event.getFrom(), event.getTo())) {
                findNextFloor(from, BlockFace.UP).ifPresent(base -> teleportToFloor(player, base));
            }
        }

    }

    @EventHandler
    public void onElevatorDown(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (Stream.of("yukielevator.use", "yukielevator.down").noneMatch(player::hasPermission)) {
            return;
        }

        Block from = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (isFloor(from)) {
            if (event.isSneaking()) {
                findNextFloor(from, BlockFace.DOWN).ifPresent(base -> teleportToFloor(player, base));
            }
        }
    }
}
