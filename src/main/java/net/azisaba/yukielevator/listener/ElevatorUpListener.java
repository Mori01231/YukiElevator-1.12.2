package net.azisaba.yukielevator.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.azisaba.yukielevator.YukiElevator;

public class ElevatorUpListener implements Listener {

    private final YukiElevator plugin;

    public ElevatorUpListener(YukiElevator plugin) {
        this.plugin = plugin;
    }

    private boolean hasPerms(Player player) {
        if (player.hasPermission("yukielevator.use")) {
            return true;
        } else if (player.hasPermission("yukielevator.up")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPlayerJumping(Player player, Location moveFrom, Location moveTo) {
        if (player.isDead()) {
            return false;
        } else if (player.isOnGround()) {
            return false;
        } else if (player.isFlying()) {
            return false;
        } else {
            return moveFrom.getY() < moveTo.getY() && player.getVelocity().getY() > 0;
        }
    }

    @EventHandler
    public void onElevatorUp(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Block baseFrom = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (plugin.getSystem().isFloor(baseFrom)) {
            return;
        } else if (isPlayerJumping(player, event.getFrom(), event.getTo())) {
            return;
        }

        Block baseTo = plugin.getSystem().tryFindFloor(baseFrom, BlockFace.UP);
        if (baseTo == null) {
            return;
        } else if (!hasPerms(player)) {
            player.sendMessage(ChatColor.RED + "あなたはエレベーターを上る権限を持っていません！");
            return;
        }

        plugin.getSystem().teleportToFloor(player, baseFrom, baseTo);
    }
}
