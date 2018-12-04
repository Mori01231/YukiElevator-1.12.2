package net.azisaba.yukielevator.listener;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import lombok.RequiredArgsConstructor;

import net.azisaba.yukielevator.YukiElevator;
import net.azisaba.yukielevator.config.ConfigParticle;
import net.azisaba.yukielevator.config.ConfigSound;

@RequiredArgsConstructor
public class ElevatorListener implements Listener {

    private final YukiElevator plugin;

    @EventHandler
    public void onElevatorUp(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!(player.hasPermission("yukielevator.use") || player.hasPermission("yukielevator.up"))) {
            return;
        }

        Location location = player.getLocation();
        if (!plugin.getElevatorSystem().isOnElevator(location)) {
            return;
        }

        if (!player.isFlying() && !player.isOnGround() && event.getFrom().getY() != event.getTo().getY() && player.getVelocity().getY() > 0) {
            Optional<Location> destination = plugin.getElevatorSystem().searchAbove(location);
            if (!destination.isPresent()) {
                return;
            }
            player.teleport(destination.get());

            ConfigSound sound = plugin.getPluginConfig().getSoundOnUp();
            player.playSound(destination.get(), sound.getType(), sound.getVolume(), sound.getPitch());

            ConfigParticle particle = plugin.getPluginConfig().getParticleOnUp();
            player.getWorld().spawnParticle(particle.getType(), destination.get(), particle.getCount(), particle.getOffsetX(), particle.getOffsetY(), particle.getOffsetZ(), particle.getExtra());
        }
    }

    @EventHandler
    public void onElevatorDown(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!(player.hasPermission("yukielevator.use") || player.hasPermission("yukielevator.down"))) {
            return;
        }

        Location location = player.getLocation();
        if (!plugin.getElevatorSystem().isOnElevator(location)) {
            return;
        }

        if (event.isSneaking()) {
            Optional<Location> destination = plugin.getElevatorSystem().searchBelow(location);
            if (!destination.isPresent()) {
                return;
            }
            player.teleport(destination.get());

            ConfigSound sound = plugin.getPluginConfig().getSoundOnDown();
            player.playSound(destination.get(), sound.getType(), sound.getVolume(), sound.getPitch());

            ConfigParticle particle = plugin.getPluginConfig().getParticleOnDown();
            player.getWorld().spawnParticle(particle.getType(), destination.get(), particle.getCount(), particle.getOffsetX(), particle.getOffsetY(), particle.getOffsetZ(), particle.getExtra());
        }
    }
}
