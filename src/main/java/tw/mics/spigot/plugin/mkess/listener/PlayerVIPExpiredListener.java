package tw.mics.spigot.plugin.mkess.listener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.cheesesoftware.PowerfulPermsAPI.CachedGroup;
import com.github.cheesesoftware.PowerfulPermsAPI.PermissionManager;
import com.github.cheesesoftware.PowerfulPermsAPI.PowerfulPermsPlugin;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import net.md_5.bungee.api.ChatColor;
import tw.mics.spigot.plugin.mkess.MkEss;

public class PlayerVIPExpiredListener extends MyListener {
    final PowerfulPermsPlugin ppplugin;
    PermissionManager permissionManager;
    DateFormat dataformat;
    public PlayerVIPExpiredListener(MkEss instance) {
        super(instance);
        ppplugin = (PowerfulPermsPlugin) Bukkit.getPluginManager().getPlugin("PowerfulPerms");
        permissionManager = ppplugin.getPermissionManager();
        dataformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        ListenableFuture<LinkedHashMap<String, List<CachedGroup>>> future = permissionManager.getPlayerOwnGroups(uuid);
        future.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    LinkedHashMap<String, List<CachedGroup>> unknow = future.get();
                    if(unknow.keySet().isEmpty()) return;
                    List<CachedGroup> groups = unknow.get(unknow.keySet().toArray()[0]);
                    for(CachedGroup group : groups){
                        if(group.willExpire()){
                            Calendar now = Calendar.getInstance();
                            now.add(Calendar.DAY_OF_MONTH, +7);
                            Date after_weeks = now.getTime();
                            Date date = group.getExpirationDate();
                            if(after_weeks.after(date)){
                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
                                    @Override
                                    public void run() {
                                        event.getPlayer().sendMessage(ChatColor.GOLD + 
                                                "提醒您, 您的 VIP 將於 " + dataformat.format(date) + " 到期");
                                        event.getPlayer().sendMessage(ChatColor.GOLD + "請務必記得將防噴背包的物品取出, Mk.II 感謝您的支持.");
                                    }  
                                }, 120);
                            }
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

        }, MoreExecutors.sameThreadExecutor());
    }
}
