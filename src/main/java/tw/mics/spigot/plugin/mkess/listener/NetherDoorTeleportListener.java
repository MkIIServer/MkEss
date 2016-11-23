package tw.mics.spigot.plugin.mkess.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import tw.mics.spigot.plugin.mkess.MkEss;

public class NetherDoorTeleportListener extends MyListener {
    public NetherDoorTeleportListener(MkEss instance) {
        super(instance);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (e.getCause() == TeleportCause.NETHER_PORTAL) {
            Player p = e.getPlayer();
            Location l = e.getFrom();
            plugin.netherdoor.setTeleportLocation(p, l);
        }
    }
}
