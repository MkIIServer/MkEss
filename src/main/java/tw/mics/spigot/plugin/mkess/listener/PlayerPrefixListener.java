package tw.mics.spigot.plugin.mkess.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.milkbowl.vault.chat.Chat;
import tw.mics.spigot.plugin.mkess.MkEss;

public class PlayerPrefixListener extends MyListener {
    private Chat chat;

    public PlayerPrefixListener(MkEss instance) {
        super(instance);
        RegisteredServiceProvider<Chat> chat = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        this.chat = chat.getProvider();
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){
            @Override
            public void run() {
                Player p = event.getPlayer();
                String str = chat.getPlayerPrefix(p);
                setPrefix(p, str);
            }
        }, 10);
    }
    

    @SuppressWarnings("deprecation")
    private void setPrefix(Player p, String prefix){
        if(prefix != null){
            prefix = ChatColor.translateAlternateColorCodes('&', prefix);
            if(prefix.length() > 16) prefix = prefix.substring(0, 16);
            Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
            Team team = board.getTeam(p.getName());
            if (team == null) {
              team = board.registerNewTeam(p.getName());
            }
            team.setPrefix(prefix);
            team.addPlayer(p);
            
            for(Player online : Bukkit.getOnlinePlayers()){
                online.setScoreboard(board);
            }
        }
    }
}
