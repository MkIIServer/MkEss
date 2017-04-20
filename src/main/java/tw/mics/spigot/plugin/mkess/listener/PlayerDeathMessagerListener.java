package tw.mics.spigot.plugin.mkess.listener;

import static com.comphenix.protocol.PacketType.Play.Server.CHAT;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import tw.mics.spigot.plugin.mkess.MkEss;
import tw.mics.spigot.plugin.mkess.config.Config;

public class PlayerDeathMessagerListener extends MyListener  {

    private Location death_location;
    private ProtocolManager manager;

    public PlayerDeathMessagerListener(MkEss instance) {
        super(instance);
        this.manager = ProtocolLibrary.getProtocolManager(); 
        manager.addPacketListener(constructProtocol(MkEss.getInstance()));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        saveDeathLocation(e.getEntity().getLocation());
    }

    private void saveDeathLocation(Location death_location) {
        this.death_location = death_location;
    }
    
    private boolean checkDeathMessageDisplay(Player show_player){
        if(show_player.getLocation().getWorld() == this.death_location.getWorld()){
            if(show_player.getLocation().distance(this.death_location) < Config.DEATH_DISTANCE_DISTANCE.getDouble())
                return true;
        }
        return false;
    }
     
    // Packets that update remote player entities
    private static final PacketType[] CHAT_PACKETS = { CHAT };
    
    // Listen PacketSending
    private PacketAdapter constructProtocol(MkEss plugin) {
        return new PacketAdapter(plugin, CHAT_PACKETS) {
            @Override
            public void onPacketSending(PacketEvent event){
                Player p = event.getPlayer();
                WrappedChatComponent msg = event.getPacket().getChatComponents().read(0);
                JSONParser parser = new JSONParser();
                MkEss.getInstance().log(msg.getJson());
                try {
                    JSONObject jsonObject = (JSONObject)(parser.parse(msg.getJson()));
                    if(jsonObject.get("translate") instanceof String){
                        String tran = (String)jsonObject.get("translate");
                        if(tran.startsWith("death")){
                            JSONArray with = (JSONArray) jsonObject.get("with");
                            JSONObject obj = (JSONObject) with.get(0);
                            String death_player_name = (String)obj.get("insertion");
                            if(Bukkit.getPlayer(death_player_name) == null || !checkDeathMessageDisplay(p))
                                event.setCancelled(true);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
