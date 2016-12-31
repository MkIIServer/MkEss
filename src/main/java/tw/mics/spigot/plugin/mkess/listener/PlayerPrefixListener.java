package tw.mics.spigot.plugin.mkess.listener;

import java.util.Set;

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
    static private PlayerPrefixListener instance;

    public PlayerPrefixListener(MkEss instance) {
        super(instance);
        RegisteredServiceProvider<Chat> chat = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if(chat == null){
            plugin.log("Do not have permission plugin, prefix system disable.");
            return;
        }
        this.chat = chat.getProvider();
        PlayerPrefixListener.instance = this;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(this.chat == null) return;
        setPlayerPrefix(event.getPlayer());
    }
    
    static public void setPlayerPrefix(Player p){
        //set scoreboard
        p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        
        new Thread(new Runnable(){
            @Override
            public void run() {
                String str = instance.chat.getPlayerPrefix(p);
                instance.setPrefix(p, str);
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
        } else {
            for(Team team: getTeams()){
                team.removeEntry(p.getName());
            }
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
    
    private Set<Team> getTeams() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Set<Team> teams = board.getTeams();
        return teams;
    }
}
