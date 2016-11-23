package tw.mics.spigot.plugin.mkess.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;

import tw.mics.spigot.plugin.mkess.MkEss;
import tw.mics.spigot.plugin.mkess.config.Config;

public class LiquidLimitListener extends MyListener {
    public LiquidLimitListener(MkEss instance) {
        super(instance);
    }

    // 防止岩漿流動
    @EventHandler
    public void onLavaFlow(BlockFromToEvent e) {
        if (!(e.getBlock().getType() == Material.STATIONARY_LAVA || e.getBlock().getType() == Material.LAVA))
            return;

        final int lava_high_limit = Config.LAVA_FLOW_HIGH_LIMIT.getInt();

        if (lava_high_limit != -1) {
            boolean flag_deny = true;
            Location l = e.getToBlock().getLocation();
            for (int i = 0; i < lava_high_limit; i++) {
                l.add(0, -1, 0);
                Material block_type = l.getBlock().getType();
                if (block_type != Material.AIR) {
                    flag_deny = false;
                    break;
                }
            }
            if (flag_deny) {
                e.setCancelled(true);
            }
        }
    }

    // 防止水流動
    @EventHandler
    public void onWaterFlow(BlockFromToEvent e) {
        final int water_high_limit = Config.WATER_FLOW_HIGH_LIMIT.getInt();
        if (water_high_limit != -1) {
            if (!(e.getBlock().getType() == Material.STATIONARY_WATER || e.getBlock().getType() == Material.WATER))
                return;

            boolean flag_deny = true;
            Location l = e.getToBlock().getLocation();
            for (int i = 0; i < water_high_limit; i++) {
                l.add(0, -1, 0);
                Material block_type = l.getBlock().getType();
                if (block_type != Material.AIR) {
                    flag_deny = false;
                    break;
                }
            }
            if (flag_deny) {
                e.setCancelled(true);
            }
        }
    }
}
