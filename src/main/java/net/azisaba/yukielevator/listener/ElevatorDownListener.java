package net.azisaba.yukielevator.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import net.azisaba.yukielevator.YukiElevator;
import org.bukkit.util.Vector;

public class ElevatorDownListener implements Listener {

    private final YukiElevator plugin;

    public ElevatorDownListener(YukiElevator plugin) {
        this.plugin = plugin;
    }

    public YukiElevator getPlugin() {
        return plugin;
    }

    @EventHandler
    public void onElevatorDown(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if ( !event.isSneaking() ) {
            return;
        }

        Block baseFrom = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if ( !plugin.getSystem().isFloor(baseFrom) ) {
            return;
        }

        Block baseTo = plugin.getSystem().tryFindFloor(baseFrom, new Vector(0,-1,0));
        if ( baseTo == null ) {
            return;
        }

        if ( !player.hasPermission("yukielevator.down") ) {
            player.sendMessage(ChatColor.RED + "あなたはエレベーターを下る権限を持っていません！");
            return;
        }

        Location playerFrom = player.getLocation();
        Location playerTo = plugin.getSystem().calculatePlayerTo(playerFrom, baseFrom, baseTo);

        player.teleport(playerTo);
        player.playSound(playerTo, Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
        player.getWorld().spawnParticle(Particle.TOTEM, playerTo, 10, 0.2, 0.2, 0.2, 0.5);
    }
}
