package tw.mics.spigot.plugin.mkess.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import tw.mics.spigot.plugin.mkess.MkEss;
import tw.mics.spigot.plugin.mkess.config.Config;

public class SpeedElytraLimitListener extends MyListener {
    public SpeedElytraLimitListener(MkEss instance) {
        super(instance);
    }

    // 限制鞘翅速度
    @EventHandler
    public void onPlayerFly(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(!p.isGliding())return;
        Vector v = p.getVelocity();
        Double speed = Math.sqrt(Math.pow(v.getX(), 2.0) + Math.pow(v.getZ(), 2.0));
        if(speed > Config.ELYTRA_SPEED_LIMIT_SPEED.getDouble())
            p.setVelocity( v.multiply(Config.ELYTRA_SPEED_LIMIT_SPEED.getDouble()/speed) );
    }
}
