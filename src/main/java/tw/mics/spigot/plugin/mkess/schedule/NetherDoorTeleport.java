package tw.mics.spigot.plugin.mkess.schedule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import tw.mics.spigot.plugin.mkess.MkEss;

public class NetherDoorTeleport {
    MkEss plugin;
    Runnable runnable;
    HashSet<Player> in_portal_list;
    HashMap<Player, Location> teleport_location;
    Iterator<? extends Player> iter;

    int schedule_id;
    int part;

    public NetherDoorTeleport(MkEss i) {
        this.plugin = i;
        part = 0;
        iter = Bukkit.getServer().getOnlinePlayers().iterator();
        in_portal_list = new HashSet<Player>();
        teleport_location = new HashMap<Player, Location>();
        setupRunnable();
    }

    public void setTeleportLocation(Player p, Location l) {
        teleport_location.put(p, l);
    }

    private void setupRunnable() {
        runnable = new Runnable() {
            public void run() {
                int player_count = Bukkit.getServer().getOnlinePlayers().size();
                int delay = 200;
                if(Bukkit.getServer().getOnlinePlayers().size() > 0){
                    if (!iter.hasNext()) {
                        iter = Bukkit.getServer().getOnlinePlayers().iterator();
                    }
                    Player p = iter.next();
                    if (p.getLocation().getBlock().getType() == Material.PORTAL
                            && p.getWorld().getEnvironment() == Environment.NORMAL) {
                        if (in_portal_list.contains(p)) {
                            p.teleport(teleport_location.get(p));
                            in_portal_list.remove(p);
                        } else {
                            in_portal_list.add(p);
                        }
                    } else {
                        if (in_portal_list.contains(p))
                            in_portal_list.remove(p);
                    }
                    delay = 200 / player_count;
                }
                if (delay < 1)
                    delay = 1;
                schedule_id = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
            }
        };
        schedule_id = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, runnable, 0);
        this.plugin.logDebug(this.getClass().getName() + " timer task removed");
    }
}
