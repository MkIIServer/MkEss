package tw.mics.spigot.plugin.mkess;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import tw.mics.spigot.plugin.mkess.command.KillCommand;
import tw.mics.spigot.plugin.mkess.config.Config;
import tw.mics.spigot.plugin.mkess.listener.AntiTowerListener;
import tw.mics.spigot.plugin.mkess.listener.DeathMessageListener;
import tw.mics.spigot.plugin.mkess.listener.LiquidLimitListener;
import tw.mics.spigot.plugin.mkess.listener.NetherDoorTeleportListener;
import tw.mics.spigot.plugin.mkess.schedule.NetherDoorTeleport;

public class MkEss extends JavaPlugin {
    private static MkEss INSTANCE;
    public NetherDoorTeleport netherdoor;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Config.load();
        new AntiTowerListener(this);
        new DeathMessageListener(this);
        new LiquidLimitListener(this);
        new NetherDoorTeleportListener(this);
        netherdoor = new NetherDoorTeleport(this);
        this.getCommand("kill").setExecutor(new KillCommand(this));
    }

    @Override
    public void onDisable() {
        this.logDebug("Unregister Listener!");
        HandlerList.unregisterAll();
        this.logDebug("Unregister Schedule tasks!");
        this.getServer().getScheduler().cancelAllTasks();
    }

    public static MkEss getInstance() {
        return INSTANCE;
    }

    // log system
    public void log(String str, Object... args) {
        String message = String.format(str, args);
        getLogger().info(message);
    }

    public void logDebug(String str, Object... args) {
        if (Config.DEBUG.getBoolean()) {
            String message = String.format(str, args);
            getLogger().info("(DEBUG) " + message);
        }
    }
}
