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

@RequiredArgsConstructor
public class ElevatorUpListener implements Listener {

	private final YukiElevator plugin;

	public boolean isPlayerJumping(Player player, Location moveFrom, Location moveTo) {
		return !player.isDead() && !player.isOnGround() && !player.isFlying() && moveFrom.getY() < moveTo.getY() && player.getVelocity().getY() > 0;
	}

	@EventHandler
	public void onElevatorUp(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		Block baseFrom = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		if (!plugin.getSystem().isFloor(baseFrom)) {
			return;
		}
		if (!isPlayerJumping(player, event.getFrom(), event.getTo())) {
			return;
		}

		plugin.getSystem().tryFindFloor(baseFrom, BlockFace.UP).ifPresent(baseTo -> {
			if (!player.hasPermission("yukielevator.use") && !player.hasPermission("yukielevator.up")) {
				player.sendMessage(ChatColor.RED + "あなたはエレベーターを上る権限を持っていません！");
				return;
			}

			plugin.getSystem().teleportToFloor(player, baseFrom, baseTo);
		});
	}
}
