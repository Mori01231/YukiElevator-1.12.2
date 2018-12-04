package net.azisaba.yukielevator.base;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import lombok.RequiredArgsConstructor;

import net.azisaba.yukielevator.YukiElevator;

@RequiredArgsConstructor
public class ElevatorSystem {

    private final YukiElevator plugin;

    public boolean isOnElevator(Location location) {
        return location.subtract(0, plugin.getPluginConfig().getBaseBlockHeight(), 0).getBlock().getType() == plugin.getPluginConfig().getBaseBlockType();
    }

    public boolean isFloor(Block block) {
        return block.getType() == plugin.getPluginConfig().getBaseBlockType() && block.getRelative(BlockFace.UP).isEmpty() && block.getRelative(BlockFace.UP, 2).isEmpty();
    }

    public Optional<Location> searchAbove(Location location) {
        Location target = location.clone();

        double fromY = location.getBlockY() + plugin.getPluginConfig().getBaseBlockHeight();
        double toY = location.getWorld().getMaxHeight();
        for (double y = fromY; y < toY; y++) {
            target.setY(y);
            Block block = target.getBlock();
            if (isFloor(block)) {
                target.add(0, plugin.getPluginConfig().getBaseBlockHeight(), 0);
                return Optional.of(target);
            } else if (!block.isEmpty()) {
                break;
            }
        }
        return Optional.empty();
    }

    public Optional<Location> searchBelow(Location location) {
        Location target = location.clone();

        double fromY = location.getY() - plugin.getPluginConfig().getBaseBlockHeight() - 1;
        double toY = 0;
        for (double y = fromY; y >= toY; y--) {
            target.setY(y);
            Block block = target.getBlock();
            if (isFloor(block)) {
                target.add(0, plugin.getPluginConfig().getBaseBlockHeight(), 0);
                return Optional.of(target);
            } else if (!block.isEmpty()) {
                break;
            }
        }
        return Optional.empty();
    }
}
