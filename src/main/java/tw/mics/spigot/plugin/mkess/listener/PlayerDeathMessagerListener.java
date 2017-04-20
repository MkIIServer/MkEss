package tw.mics.spigot.plugin.mkess.listener;

import static com.comphenix.protocol.PacketType.Play.Server.CHAT;

import java.util.UUID;

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
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import tw.mics.spigot.plugin.mkess.MkEss;
import tw.mics.spigot.plugin.mkess.config.Config;

public class PlayerDeathMessagerListener extends MyListener  {

    private Location location;
    private UUID death_player_uuid;
    private ProtocolManager manager;
    private PacketListener protocolListener;

    public PlayerDeathMessagerListener(MkEss instance) {
        super(instance);
        this.manager = ProtocolLibrary.getProtocolManager(); 
        manager.addPacketListener(protocolListener = constructProtocol(MkEss.getInstance()));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        savedeathmessage(e.getEntity().getLocation(), e.getEntity().getUniqueId());
    }

    private void savedeathmessage(Location location, UUID death_player_uuid) {
        this.location = location;
        this.death_player_uuid = death_player_uuid;
    }
    
    private boolean removethismessage(Player show_player, UUID deaht_player_uuid){
        if(deaht_player_uuid.equals(this.death_player_uuid))
            return false;
        if(show_player.getLocation().getWorld() == this.location.getWorld()){
            if(show_player.getLocation().distance(location) < Config.DEATH_DISTANCE_DISTANCE.getDouble())
                return false;
        }
        return true;
    }

    public void close() {
        if (manager != null) {
            manager.removePacketListener(protocolListener);
            manager = null;
        }
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
                try {
                    JSONObject jsonObject = (JSONObject)(parser.parse(msg.getJson()));
                    if(jsonObject.get("translate") instanceof String){
                        String tran = (String)jsonObject.get("translate");
                        if(tran.startsWith("death")){
                            JSONArray with = (JSONArray) jsonObject.get("with");
                            JSONObject obj = (JSONObject) with.get(0);
                            String death_player_name = (String)obj.get("insertion");
                            MkEss.getInstance().log(death_player_name);
                            if(removethismessage(p, Bukkit.getPlayer(death_player_name).getUniqueId()))
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
