package net.azisaba.yukielevator.util;

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
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ElevatorSystem {

	private final Material baseType;
	private final int height;

	public boolean isSafe(Block block) {
		return block.getType().isTransparent();
	}

	public boolean isFloor(Block base) {
		return base.getType() == baseType && IntStream.range(1, height).mapToObj(i -> base.getRelative(BlockFace.UP, i)).allMatch(this::isSafe);
	}

	public Optional<Block> tryFindFloor(Block baseFrom, BlockFace face) {
		Vector direction = new Vector(face.getModX(), face.getModY(), face.getModZ());
		Location loc = baseFrom.getLocation().setDirection(direction);
		int maxDistance = baseFrom.getWorld().getMaxHeight();

		BlockIterator it = new BlockIterator(loc, 0, maxDistance);
		Iterators.advance(it, height);

		List<Block> list = Lists.newArrayList(it);
		int limit = Iterables.indexOf(list, Predicates.and(Predicates.not(this::isFloor), Predicates.not(this::isSafe)));

		return Iterables.tryFind(Iterables.limit(list, limit == -1 ? maxDistance : limit), this::isFloor).toJavaUtil();
	}

	public void teleportToFloor(Player player, Block baseFrom, Block baseTo) {
		Location from = player.getLocation();
		Location to = baseTo.getRelative(BlockFace.UP).getLocation();

		Vector relativeXZ = from.clone().subtract(baseFrom.getLocation()).toVector().setY(0);
		to.add(relativeXZ);

		Vector direction = from.getDirection();
		to.setDirection(direction);

		player.teleport(to);
		player.playSound(to, Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
		player.getWorld().spawnParticle(Particle.TOTEM, to, 50, 0.2, 0.2, 0.2, 0.5);
	}
}
