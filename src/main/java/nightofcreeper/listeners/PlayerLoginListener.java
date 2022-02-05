package nightofcreeper.listeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerLoginListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH , ignoreCancelled = true)
    public void EntitySpawnEventHandler(PlayerLoginEvent event) {

        ItemStack ch = new ItemStack(Material.CREEPER_HEAD, 1);
        ch.addEnchantment(Enchantment.BINDING_CURSE, 1);
        ch.addEnchantment(Enchantment.VANISHING_CURSE, 1);
        
        event.getPlayer().getInventory().setHelmet(ch);      
    }


    @EventHandler(priority = EventPriority.HIGH , ignoreCancelled = true)
    public void RespawnSpawnEventHandler(PlayerRespawnEvent event) {
        ItemStack ch = new ItemStack(Material.CREEPER_HEAD, 1);
        ch.addEnchantment(Enchantment.BINDING_CURSE, 1);
        ch.addEnchantment(Enchantment.VANISHING_CURSE, 1);        
        event.getPlayer().getInventory().setHelmet(ch);       
    }

    
}
