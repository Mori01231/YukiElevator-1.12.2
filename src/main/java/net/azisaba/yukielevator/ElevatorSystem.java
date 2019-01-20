package net.azisaba.yukielevator;

import java.util.stream.IntStream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class ElevatorSystem {

    private final Material baseType;
    private final int height;

    public ElevatorSystem(ElevatorConfig config) {
        this.baseType = config.getSettings().getBaseBlockType();
        this.height = config.getSettings().getElevatorHeight();
    }

    public boolean isSafe(Block block) {
        return block.getType().isTransparent();
    }

    public boolean isFloor(Block base) {
        if (base.getType() != baseType) {
            return false;
        } else {
            return IntStream.range(0, height).mapToObj(i -> base.getRelative(BlockFace.UP, i)).allMatch(this::isSafe);
        }
    }

    public Block tryFindFloor(Block baseFrom, BlockFace face) {
        Vector direction = face.getDirection();
        Location loc = baseFrom.getLocation().setDirection(direction);

        BlockIterator it = new BlockIterator(loc);
        IntStream.range(0, height).forEach(i -> it.next());

        while (it.hasNext()) {
            Block baseTo = it.next();
            if (isFloor(baseTo)) {
                return baseTo;
            } else if (!isSafe(baseTo)) {
                break;
            }
        }
        return null;
    }

    public void teleportToFloor(Player player, Block baseFrom, Block baseTo) {
        Location from = player.getLocation();
        Location to = baseTo.getRelative(BlockFace.UP).getLocation();

        Vector relativeXZ = from.clone().subtract(baseFrom.getLocation()).toVector().setY(0);
        to.add(relativeXZ);

        Vector direction = from.getDirection();
        to.setDirection(direction);

        player.teleport(to);
        player.playSound(to, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        player.getWorld().spawnParticle(Particle.TOTEM, to, 50, 0.2, 0.2, 0.2, 0.5);
    }
}
