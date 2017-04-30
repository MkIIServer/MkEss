package tw.mics.spigot.plugin.mkess.listener;

import java.util.Iterator;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import tw.mics.spigot.plugin.mkess.MkEss;
import tw.mics.spigot.plugin.mkess.config.Config;

public class VillagerTradeLimitListener extends MyListener {
    public VillagerTradeLimitListener(MkEss instance) {
        super(instance);
    }

    @EventHandler
    public void onTrade(VillagerReplenishTradeEvent e) {
        MerchantRecipe recipe = e.getRecipe();
        Integer recipe_index = null;
        List<ItemStack> items = recipe.getIngredients();
        
        List<MerchantRecipe> recipes = e.getEntity().getRecipes();
        
        for(int i=0; i<recipes.size(); i++){
            MerchantRecipe rec = recipes.get(i);
            if(rec.equals(recipe)){
                recipe_index = i;
            }
        }
        
        Iterator<ItemStack> itr = items.iterator();
        while(itr.hasNext()){
            ItemStack item = itr.next();
            if(Config.VILLAGER_TRADE_LIMIT_ADD_REQUIRE_ITEM_CHANCE.getDouble() > Math.random()){
                int amount = item.getAmount() + Config.VILLAGER_TRADE_LIMIT_ADD_AMOUNT.getInt();
                if(amount > 64) amount = 64;
                item.setAmount(amount);
            }
        }
        
        recipe.setIngredients(items);
        e.getEntity().setRecipe(recipe_index, recipe);
    }
}
