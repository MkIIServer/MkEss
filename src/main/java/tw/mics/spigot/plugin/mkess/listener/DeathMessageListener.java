package tw.mics.spigot.plugin.mkess.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import tw.mics.spigot.plugin.mkess.MkEss;

public class DeathMessageListener extends MyListener {
    public DeathMessageListener(MkEss instance) {
        super(instance);
    }

    // 移除殺人訊息
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (p.hasMetadata("suicide")) {
            p.removeMetadata("suicide", plugin);
        } else {
            plugin.log(e.getDeathMessage());
        }
        e.setDeathMessage("");
    }
}
