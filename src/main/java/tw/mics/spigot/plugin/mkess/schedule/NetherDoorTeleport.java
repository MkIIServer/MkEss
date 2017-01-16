package tw.mics.spigot.plugin.mkess.schedule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import tw.mics.spigot.plugin.mkess.MkEss;

public class NetherDoorTeleport {
    MkEss plugin;
    Runnable runnable;
    HashSet<UUID> in_portal_list_once;
    HashSet<UUID> in_portal_list_twice;
    HashMap<UUID, TeleportLocation> teleport_location;
    Iterator<? extends Player> iter;

    int schedule_id;
    int part;
    class TeleportLocation {
        Location from;
        Location to;
        TeleportLocation(Location from, Location to){
            this.from=from;
            this.to=to;
        }
    }

    public NetherDoorTeleport(MkEss i) {
        this.plugin = i;
        part = 0;
        iter = Bukkit.getServer().getOnlinePlayers().iterator();
        in_portal_list_once = new HashSet<UUID>();
        in_portal_list_twice = new HashSet<UUID>();
        teleport_location = new HashMap<UUID, TeleportLocation>();
        setupRunnable();
    }

    public void setTeleportLocation(Player p, Location from, Location to) {
        teleport_location.put(p.getUniqueId(), new TeleportLocation(from, to));
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
                    if (
                            p.getLocation().getBlock().getType() == Material.PORTAL &&
                            p.getWorld().getEnvironment() == Environment.NORMAL
                        ) {
                        TeleportLocation tl = teleport_location.get(p.getUniqueId());
                        if (
                                tl != null && 
                                tl.to.getWorld() == p.getWorld() && 
                                tl.to.distance(p.getLocation()) < 10
                        ) {
                            if(in_portal_list_twice.contains(p.getUniqueId())){
                                p.teleport(tl.from);
                                in_portal_list_once.remove(p.getUniqueId());
                                in_portal_list_twice.remove(p.getUniqueId());
                                teleport_location.remove(p.getUniqueId());
                            } else if(in_portal_list_once.contains(p.getUniqueId())){
                                p.sendMessage("已偵測到您可能被地獄門卡住, 將在數秒後回傳");
                                in_portal_list_twice.add(p.getUniqueId());
                            } else {
                                in_portal_list_once.add(p.getUniqueId());
                            }
                        } else {
                            in_portal_list_once.remove(p.getUniqueId());
                            in_portal_list_twice.remove(p.getUniqueId());
                            teleport_location.remove(p.getUniqueId());
                        }
                    } else{
                        in_portal_list_once.remove(p.getUniqueId());
                        in_portal_list_twice.remove(p.getUniqueId());
                    }
                    delay = 100 / player_count;
                }
                if (delay < 1)
                    delay = 1;
                schedule_id = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
            }
        };
        schedule_id = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, runnable, 0);
        this.plugin.logDebug(this.getClass().getName() + " timer task start");
    }
}
