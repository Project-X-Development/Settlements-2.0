package me.projectx.Settlements.Events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener{
	
	@EventHandler
	public void onClick(InventoryClickEvent e){
		if (e.getInventory().getTitle().contains(ChatColor.BLUE + "Members of "))
			e.setCancelled(true);
		
		else if (e.getInventory().getTitle().equals(ChatColor.BLUE + "All Current Settlements:"))
			e.setCancelled(true);
		
		else if (e.getInventory().getTitle().equals(ChatColor.DARK_RED + "Settlement Commands"))
			e.setCancelled(true);
	}	
}
