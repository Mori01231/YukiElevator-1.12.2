package net.azisaba.yukielevator.listener;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import net.azisaba.yukielevator.YukiElevator;

public class ElevatorDownListener implements Listener {

    private final YukiElevator plugin;

    public ElevatorDownListener(YukiElevator plugin) {
        this.plugin = plugin;
    }

    private boolean hasPerms(Player player) {
        if (player.hasPermission("yukielevator.use")) {
            return true;
        } else if (player.hasPermission("yukielevator.down")) {
            return true;
        } else {
            return false;
        }
    }

    @EventHandler
    public void onElevatorDown(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        Block baseFrom = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (plugin.getSystem().isFloor(baseFrom)) {
            return;
        } else if (event.isSneaking()) {
            return;
        }

        Block baseTo = plugin.getSystem().tryFindFloor(baseFrom, BlockFace.DOWN);
        if (baseTo == null) {
            return;
        } else if (!hasPerms(player)) {
            player.sendMessage(ChatColor.RED + "あなたはエレベーターを下る権限を持っていません！");
            return;
        }

        plugin.getSystem().teleportToFloor(player, baseFrom, baseTo);
    }
}
