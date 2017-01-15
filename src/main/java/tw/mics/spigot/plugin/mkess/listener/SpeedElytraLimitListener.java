package tw.mics.spigot.plugin.mkess.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import tw.mics.spigot.plugin.mkess.MkEss;

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
        //plugin.log(speed.toString());
        if(speed > 1)p.setVelocity( v.multiply(1/speed) );
    }
}
