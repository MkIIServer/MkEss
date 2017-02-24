package tw.mics.spigot.plugin.mkess.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import tw.mics.spigot.plugin.mkess.MkEss;

public enum Config {

	DEBUG("debug", false, "is plugin show debug message?"),
    LIQUIDLIMIT_ENABLE("liquidlimit.enable", true, ""),
    LIQUIDLIMIT_LAVA_FLOW_HIGH_LIMIT("liquidlimit.lava-flow-limit", 2, "-1 to disable, 0 to limit all lava flow"),
    LIQUIDLIMIT_WATER_FLOW_HIGH_LIMIT("liquidlimit.water-flow-limit", 4, "-1 to disable, 0 to limit all water flow"),
    
    WORLD_SETTING_ENABLE("world-setting.enable", true, ""),
    WORLD_SETTING_WORLD_BORDER_SIZE("world-setting.world-size", 48000, ""),
    WORLD_SETTING_NETHER_BORDER_SIZE("world-setting.world-nether-size", 6000, ""),
    
    PLAYER_PREFIX_ENABLE("player-prefix.enable", true, ""),
    PLAYER_PREFIX_REMOVE_SCOREBOARD_ON_START("player-prefix.remove-scoreboard-on-start", true, ""),
    
    ELYTRA_SPEED_LIMIT_ENABLE("elytra-speed-limit.enable", true, ""),
    ELYTRA_SPEED_LIMIT_SPEED("elytra-speed-limit.speed-limit", 1.0, ""),
    
    NETHERDOOR_TELEPORT_BACK_ENABLE("netherdoor-teleport-back.enable", true, ""),
    VIP_EXPIRED_NOTICE_ENABLE("vip-expired-notice.enable", true, ""),
    KILL_COMMAND_ENABLE("kill-command.enable", true, ""),;
	
	private final Object value;
	private final String path;
	private final String description;
	private static YamlConfiguration cfg;
	private static final File f = new File(MkEss.getInstance().getDataFolder(), "config.yml");
	
	private Config(String path, Object val, String description) {
	    this.path = path;
	    this.value = val;
	    this.description = description;
	}
	
	public String getPath() {
	    return path;
	}
	
	public String getDescription() {
	    return description;
	}
	
	public Object getDefaultValue() {
	    return value;
	}

	public boolean getBoolean() {
	    return cfg.getBoolean(path);
	}
	
	public int getInt() {
	    return cfg.getInt(path);
	}
	
	public double getDouble() {
	    return cfg.getDouble(path);
	}
	
	public List<String> getStringList() {
	    return cfg.getStringList(path);
	}
	
	public static void load() {
		boolean save_flag = false;
		
		MkEss.getInstance().getDataFolder().mkdirs();
        String header = "";
		cfg = YamlConfiguration.loadConfiguration(f);

        for (Config c : values()) {
            if(c.getDescription().toLowerCase().equals("removed")){
                if(cfg.contains(c.getPath())){
                    save_flag = true;
                    cfg.set(c.getPath(), null);
                }
                continue;
            }
            if(!c.getDescription().isEmpty()){
                header += c.getPath() + ": " + c.getDescription() + System.lineSeparator();
            }
            if (!cfg.contains(c.getPath())) {
            	save_flag = true;
                c.set(c.getDefaultValue(), false);
            }
        }
        cfg.options().header(header);
        
        if(save_flag){
        	save();
    		cfg = YamlConfiguration.loadConfiguration(f);
        }
	}
	
	public static void save(){
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void set(Object value, boolean save) {
	    cfg.set(path, value);
	    if (save) {
            save();
	    }
	}
}
