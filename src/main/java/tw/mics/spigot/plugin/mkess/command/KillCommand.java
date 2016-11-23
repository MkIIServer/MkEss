package tw.mics.spigot.plugin.mkess.command;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import tw.mics.spigot.plugin.mkess.MkEss;

public class KillCommand implements CommandExecutor {
    MkEss plugin;
    List<Material> blockBlockList;

    public KillCommand(MkEss i) {
        this.plugin = i;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§4this command must run on player");
            return true;
        }
        Player p = (Player) sender;
        p.setMetadata("suicide", new FixedMetadataValue(plugin, "123"));
        p.setHealth(0);
        return true;
    }
}
