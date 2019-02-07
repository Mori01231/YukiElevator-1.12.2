package net.azisaba.yukielevator.elevator;

import java.util.stream.IntStream;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import net.azisaba.yukielevator.config.ElevatorConfig;

public class ElevatorSystem {

    private final Material baseType;
    private final int height;

    public ElevatorSystem(ElevatorConfig config) {
        this.baseType = config.getBaseBlockType();
        this.height = config.getElevatorHeight();
    }

    public Material getBaseType() {
        return baseType;
    }

    public int getHeight() {
        return height;
    }

    @SuppressWarnings("deprecation")
    public boolean isSafe(Block block) {
        return block.getType().isTransparent();
    }

    public boolean isFloor(Block baseFrom) {
        if (baseFrom.getType() != baseType)
            return false;

        return IntStream.range(1, height).allMatch(distance -> isSafe(baseFrom.getRelative(BlockFace.UP, distance)));
    }

    public Block tryFindFloor(Block baseFrom, BlockFace face) {
        Vector direction = face.getDirection();
        Location loc = baseFrom.getLocation().setDirection(direction);
        int maxDistance = baseFrom.getWorld().getMaxHeight();

        BlockIterator it = new BlockIterator(loc, 0, maxDistance);
        Iterators.advance(it, height);

        for (Block baseTo : Lists.newArrayList(it))
            if (isFloor(baseTo))
                return baseTo;
            else if (!isSafe(baseTo))
                break;

        return null;
    }

    public Location calculatePlayerTo(Location playerFrom, Block baseFrom, Block baseTo) {
        Location playerTo = baseTo.getRelative(BlockFace.UP).getLocation();

        Vector relativeOnBlock = playerFrom.clone().subtract(baseFrom.getLocation()).toVector().setY(0);
        playerTo.add(relativeOnBlock);

        Vector playerDirection = playerFrom.getDirection();
        playerTo.setDirection(playerDirection);

        return playerTo;
    }
}
