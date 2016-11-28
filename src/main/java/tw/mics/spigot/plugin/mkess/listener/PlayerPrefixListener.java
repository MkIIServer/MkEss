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
        if(chat == null){
            plugin.log("Do not have permission plugin, prefix system disable.");
            return;
        }
        this.chat = chat.getProvider();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(this.chat == null) return;
        
        //set scoreboard
        event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        
        new Thread(new Runnable(){
            @Override
            public void run() {
                Player p = event.getPlayer();
                String str = chat.getPlayerPrefix(p);
                setPrefix(p, str);
            }
        }).start();
    }
    
    private void setPrefix(Player p, String prefix){
        if(prefix != null && !prefix.isEmpty()){
            prefix = ChatColor.translateAlternateColorCodes('&', prefix);
            if(prefix.length() > 16) prefix = prefix.substring(0, 16);
            Team team = getTeam(prefix);
            team.setPrefix(prefix);
            team.addEntry(p.getName());;
        }
    }
    
    private Team getTeam(String prefix) {
        String teamname = prefix.replace("&", "");
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam(teamname);
        if (team == null) {
          team = board.registerNewTeam(teamname);
          team.setCanSeeFriendlyInvisibles(false);
          team.setAllowFriendlyFire(true);
        }
        return team;
    }
}
