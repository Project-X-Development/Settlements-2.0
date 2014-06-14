package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.SettlementManager;
import me.projectx.Settlements.Models.Settlement;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener{
	
	@EventHandler
	public void onClick(InventoryClickEvent e){
		for (Settlement s: SettlementManager.getManager().settlements){
			if (e.getInventory().getTitle().equals(ChatColor.BLUE + "Members of " + s.getName()) || 
					e.getInventory().getTitle().equals(ChatColor.BLUE + "All Current Settlements:")){
				e.setCancelled(true);
			}
		}
	}	
}
