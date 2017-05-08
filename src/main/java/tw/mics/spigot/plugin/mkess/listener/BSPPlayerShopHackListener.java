package tw.mics.spigot.plugin.mkess.listener;

import static com.comphenix.protocol.PacketType.Play.Client.WINDOW_CLICK;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import net.md_5.bungee.api.ChatColor;
import tw.mics.spigot.plugin.mkess.MkEss;
import tw.mics.spigot.plugin.mkess.config.Config;

public class BSPPlayerShopHackListener extends MyListener {
    private ProtocolManager manager;
    public BSPPlayerShopHackListener(MkEss instance) {
        super(instance);
        this.manager = ProtocolLibrary.getProtocolManager(); 
        manager.addPacketListener(constructProtocol(MkEss.getInstance()));
    }

    // Packets that update remote player entities
    private static final PacketType[] CLICK_PACKETS = { WINDOW_CLICK };
    
    // Listen PacketSending
    private PacketAdapter constructProtocol(MkEss plugin) {
        return new PacketAdapter(plugin, CLICK_PACKETS) {
            @Override
            public void onPacketReceiving(PacketEvent event){
                Player p = event.getPlayer();
                Inventory inv = p.getOpenInventory().getTopInventory();
                int slot = event.getPacket().getIntegers().read(1);
                if(
                        inv.getTitle().equals(
                                ChatColor.translateAlternateColorCodes('&', 
                                        Config.BSP_PLAYER_SHOP_HACK_EDIT_MODE_NAME.getString()
                                        )
                                ) &&
                        slot >= 0 &&
                        slot < 45 &&
                        inv.getItem(slot) != null &&
                        inv.getItem(slot).getType() != Material.AIR
                        
                ){
                    event.setCancelled(true);
                    p.updateInventory();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c商店物品無法直接拿回, 請於購買模式中自行購回"));
                }
            }
        };
    }
}
