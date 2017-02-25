package tw.mics.spigot.plugin.mkess;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import tw.mics.spigot.plugin.mkess.command.KillCommand;
import tw.mics.spigot.plugin.mkess.config.Config;
import tw.mics.spigot.plugin.mkess.listener.LiquidLimitListener;
import tw.mics.spigot.plugin.mkess.listener.NetherDoorTeleportListener;
import tw.mics.spigot.plugin.mkess.listener.PlayerPrefixListener;
import tw.mics.spigot.plugin.mkess.listener.PlayerVIPExpiredListener;
import tw.mics.spigot.plugin.mkess.listener.ShulkerBoxLimitListener;
import tw.mics.spigot.plugin.mkess.listener.SpeedElytraLimitListener;
import tw.mics.spigot.plugin.mkess.schedule.NetherDoorTeleport;

public class MkEss extends JavaPlugin {
    private static MkEss INSTANCE;
    public NetherDoorTeleport netherdoor;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Config.load();
        if(true)
            new ShulkerBoxLimitListener(this);
        if(Config.LIQUIDLIMIT_ENABLE.getBoolean())
            new LiquidLimitListener(this);
        if(Config.NETHERDOOR_TELEPORT_BACK_ENABLE.getBoolean())
            new NetherDoorTeleportListener(this);
        if(Config.PLAYER_PREFIX_ENABLE.getBoolean())
            new PlayerPrefixListener(this);
        if(Config.VIP_EXPIRED_NOTICE_ENABLE.getBoolean()){
            try{
                Class.forName("com.github.cheesesoftware.PowerfulPermsAPI.PowerfulPermsPlugin");
                new PlayerVIPExpiredListener(this);
            } catch (ClassNotFoundException e) {
                this.log("Not found PowerfulPerms, disable VIP expired notify");
            }
        }
        if(Config.ELYTRA_SPEED_LIMIT_ENABLE.getBoolean())new SpeedElytraLimitListener(this);
        netherdoor = new NetherDoorTeleport(this);
        
        if(Config.WORLD_SETTING_ENABLE.getBoolean())
            WorldSetting.runsetting();
        
        if(Config.PLAYER_PREFIX_REMOVE_SCOREBOARD_ON_START.getBoolean())
            removeScoreboard();
        
        if(Config.KILL_COMMAND_ENABLE.getBoolean())
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
    

    //================ private ================
    private void removeScoreboard(){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Iterator<Team> iter = board.getTeams().iterator();
        while(iter.hasNext()){
            Team team = iter.next();
            team.unregister();
        }
    }
}
