package tw.mics.spigot.plugin.mkess.schedule;

import org.bukkit.World.Environment;

import tw.mics.spigot.plugin.mkess.MkEss;

public class NetherTopChecker {
    MkEss plugin;
    Runnable runnable;
    int schedule_id;
    public NetherTopChecker(MkEss i){
        this.plugin = i;
        setupRunnable();
    }
    
    public void removeRunnable(){
        this.plugin.getServer().getScheduler().cancelTask(schedule_id);
        this.plugin.logDebug("Nether top checke task removed");
    }
    
    private void setupRunnable(){
        runnable = new Runnable(){
            public void run() {
                plugin.getServer().getOnlinePlayers().forEach(p->{
                    if(
                            p.getWorld().getEnvironment() == Environment.NETHER &&
                            p.getLocation().getY() >= 128
                            ){
                        p.damage(10);
                    }
                });
            }
        };
        schedule_id = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, runnable, 0, 20);
        this.plugin.logDebug("Nether top checke task added");
    }
}
