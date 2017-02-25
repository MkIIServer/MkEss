package tw.mics.spigot.plugin.mkess.listener;

import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import tw.mics.spigot.plugin.mkess.MkEss;

public class ShulkerBoxLimitListener extends MyListener {
    public ShulkerBoxLimitListener(MkEss instance) {
        super(instance);
    }

    @EventHandler
    public void onPlayerClickItem(InventoryClickEvent e) {
        //put in
        ItemStack item = e.getCursor();
        if(
                item.getItemMeta() instanceof BlockStateMeta &&
                e.getClickedInventory() != null &&
                e.getClickedInventory().getType() == InventoryType.ENDER_CHEST
        ){
            BlockStateMeta im = (BlockStateMeta)item.getItemMeta();
            if(im.getBlockState() instanceof ShulkerBox){
                e.setCancelled(true);
            }
        }
        
        //shift + click put in
        item = e.getCurrentItem();
        if(
                item.getItemMeta() instanceof BlockStateMeta &&
                (
                        e.getClick() == ClickType.SHIFT_LEFT ||
                        e.getClick() == ClickType.SHIFT_RIGHT
                )&&
                e.getClickedInventory() != null &&
                e.getClickedInventory().getType() == InventoryType.PLAYER &&
                e.getWhoClicked().getOpenInventory().getType() == InventoryType.ENDER_CHEST
        ){
            BlockStateMeta im = (BlockStateMeta)item.getItemMeta();
            if(im.getBlockState() instanceof ShulkerBox){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onPlayerDropItem(InventoryDragEvent e){
        //drag in
        ItemStack item = e.getOldCursor();
        if(
                item.getItemMeta() instanceof BlockStateMeta &&
                e.getInventory() != null &&
                e.getInventory().getType() == InventoryType.ENDER_CHEST
        ){
            BlockStateMeta im = (BlockStateMeta)item.getItemMeta();
            if(im.getBlockState() instanceof ShulkerBox){
                e.setCancelled(true);
            }
        }
    }
}
