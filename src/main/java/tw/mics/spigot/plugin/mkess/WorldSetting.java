package tw.mics.spigot.plugin.mkess;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldSetting {
    static public void runsetting(){
        List<World> worlds = Bukkit.getServer().getWorlds();
        for(World world : worlds){
            switch(world.getEnvironment()){
            case NETHER:
                world.getWorldBorder().setCenter(0, 0);
                world.getWorldBorder().setSize(6000);
                break;
            default:
            }
            world.setGameRuleValue("showDeathMessages", "false");
            world.setGameRuleValue("maxEntityCramming", "8");
        }
    }
}
