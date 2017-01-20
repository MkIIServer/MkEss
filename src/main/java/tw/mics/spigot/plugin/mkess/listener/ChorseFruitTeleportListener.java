package tw.mics.spigot.plugin.mkess.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import tw.mics.spigot.plugin.mkess.MkEss;

public class ChorseFruitTeleportListener extends MyListener {
    public ChorseFruitTeleportListener(MkEss instance) {
        super(instance);
    }

    //防止歌來果傳送
    @EventHandler
    public void playerTeleportEvent(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) {
            event.setCancelled(true);
        }
    }
}
