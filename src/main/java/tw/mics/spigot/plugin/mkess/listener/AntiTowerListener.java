package tw.mics.spigot.plugin.mkess.listener;

import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import tw.mics.spigot.plugin.mkess.MkEss;

public class AntiTowerListener extends MyListener {

    public AntiTowerListener(MkEss instance) {
        super(instance);
    }

    // 怪物自然死亡不會掉落物品
    @EventHandler
    public void onMobsDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player)
            return;
        if (e.getEntity() instanceof Animals)
            return;
        if (e.getEntity().getType() == EntityType.ARMOR_STAND)
            return;
        if (e.getDroppedExp() == 0) {
            e.getDrops().clear();
        }
    }
}
