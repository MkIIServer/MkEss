package tw.mics.spigot.plugin.mkess.listener;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import tw.mics.spigot.plugin.mkess.MkEss;

public class ShulkerBoxLimitListener extends MyListener {
    public ShulkerBoxLimitListener(MkEss instance) {
        super(instance);
    }
    final Material[] blockitem_array = {
            Material.WHITE_SHULKER_BOX,
            Material.ORANGE_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX,
            Material.LIGHT_BLUE_SHULKER_BOX,
            Material.YELLOW_SHULKER_BOX,
            Material.LIME_SHULKER_BOX,
            Material.PINK_SHULKER_BOX,
            Material.GRAY_SHULKER_BOX,
            Material.SILVER_SHULKER_BOX,
            Material.CYAN_SHULKER_BOX,
            Material.PURPLE_SHULKER_BOX,
            Material.BLUE_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX,
            Material.GREEN_SHULKER_BOX,
            Material.RED_SHULKER_BOX,
            Material.BLACK_SHULKER_BOX,
    };

    @EventHandler
    public void onPlayerClickItem(InventoryClickEvent e) {
        //put in
        if(
                e.getClick() == ClickType.LEFT ||
                e.getClick() == ClickType.RIGHT
        ){
            ItemStack item = e.getCursor();
            if(
                item != null &&
                Arrays.asList(blockitem_array).contains(item.getType()) &&
                e.getClickedInventory() != null &&
                e.getClickedInventory().getType() == InventoryType.ENDER_CHEST
            ){
                e.setCancelled(true);
            }
        }
        
        //shift + click put in
        if(
                e.getClick() == ClickType.SHIFT_LEFT ||
                e.getClick() == ClickType.SHIFT_RIGHT
        ){
            ItemStack item = e.getCurrentItem();
            if(
                item != null &&
                Arrays.asList(blockitem_array).contains(item.getType()) &&
                e.getClickedInventory() != null &&
                e.getClickedInventory().getType() == InventoryType.PLAYER &&
                e.getWhoClicked().getOpenInventory().getType() == InventoryType.ENDER_CHEST
            ){
                e.setCancelled(true);
            }
        }
        
        //number key
        if(e.getClick() == ClickType.NUMBER_KEY){
            ItemStack item = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());
            if(
                item != null &&
                Arrays.asList(blockitem_array).contains(item.getType()) &&
                e.getClickedInventory().getType() == InventoryType.ENDER_CHEST
            ){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onPlayerDropItem(InventoryDragEvent e){
        //drag in
        ItemStack item = e.getOldCursor();
        if(
                item != null &&
                Arrays.asList(blockitem_array).contains(item.getType()) &&
                e.getInventory() != null &&
                e.getInventory().getType() == InventoryType.ENDER_CHEST
        ){
                e.setCancelled(true);
        }
    }
}
