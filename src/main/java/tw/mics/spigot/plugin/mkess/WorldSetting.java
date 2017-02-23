package tw.mics.spigot.plugin.mkess;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

import tw.mics.spigot.plugin.mkess.config.Config;

public class WorldSetting {
    static public void runsetting(){
        List<World> worlds = Bukkit.getServer().getWorlds();
        for(World world : worlds){
            switch(world.getEnvironment()){
            case NORMAL:
                world.getWorldBorder().setCenter(0, 0);
                world.getWorldBorder().setSize(Config.WORLD_SETTING_WORLD_BORDER_SIZE.getDouble());
                break;
            case NETHER:
                world.getWorldBorder().setCenter(0, 0);
                world.getWorldBorder().setSize(Config.WORLD_SETTING_NETHER_BORDER_SIZE.getDouble());
                break;
            default:
                break;
            }
            world.setGameRuleValue("showDeathMessages", "false");
            world.setGameRuleValue("maxEntityCramming", "8");
        }
    }
}
