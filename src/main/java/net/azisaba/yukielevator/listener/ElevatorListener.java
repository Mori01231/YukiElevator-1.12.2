package net.azisaba.yukielevator.listener;

import java.util.Optional;
import java.util.stream.IntStream;

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

    private boolean isSafeBlock(Block block) {
        return block == null || !(block.getType().isSolid() || block.isLiquid());
    }

    private boolean isFloor(Block block) {
        return block.getType() == plugin.getPluginConfig().getBaseBlockType() && IntStream.range(1, plugin.getPluginConfig().getElevatorHeight()) //
                .allMatch(distance -> isSafeBlock(block.getRelative(BlockFace.UP, distance)));
    }

    private boolean isPlayerJumping(Player player, Location moveFrom, Location moveTo) {
        return !player.isFlying() && !player.isOnGround() && moveFrom.getY() != moveTo.getY() && player.getVelocity().getY() > 0;
    }

    private Optional<Block> findNextFloor(Block from, BlockFace face) {
        for (BlockIterator it = new BlockIterator(from.getWorld(), from.getLocation().toVector(), new Vector(face.getModX(), face.getModY(), face.getModZ()), 0, from.getWorld().getMaxHeight()); it.hasNext();) {
            Block block = it.next();
            if (isFloor(block)) {
                return Optional.of(block);
            } else if (!isSafeBlock(block)) {
                break;
            }
        }
        return Optional.empty();
    }

    private void teleportToFloor(Player player, Block block) {
        Location from = player.getLocation();
        Location to = block.getRelative(BlockFace.UP).getLocation().add(0.5, 0, 0.5).setDirection(from.getDirection());
        player.teleport(to);
        player.playSound(to, Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
        player.getWorld().spawnParticle(Particle.TOTEM, to, 50, 1, 1, 1, 0.5, 0);
    }

    @EventHandler
    public void onElevatorUp(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!(player.hasPermission("yukielevator.use") || player.hasPermission("yukielevator.up"))) {
            return;
        }

        Block from = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (!isFloor(from)) {
            return;
        }

        if (isPlayerJumping(player, event.getFrom(), event.getTo())) {
            findNextFloor(from, BlockFace.UP).ifPresent(block -> teleportToFloor(player, block));
        }
    }

    @EventHandler
    public void onElevatorDown(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!(player.hasPermission("yukielevator.use") || player.hasPermission("yukielevator.down"))) {
            return;
        }

        Block from = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (!isFloor(from)) {
            return;
        }

        if (event.isSneaking()) {
            findNextFloor(from, BlockFace.DOWN).ifPresent(block -> teleportToFloor(player, block));
        }
    }
}
